package hfad.com.studytimeapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.FillFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import hfad.com.studytimeapp.R
import hfad.com.studytimeapp.data.Study
import hfad.com.studytimeapp.databinding.FragmentDayBinding
import hfad.com.studytimeapp.viewmodels.MainActivityViewModel
import kotlinx.android.synthetic.main.fragment_day.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class DayFragment : Fragment() {

    private lateinit var binding: FragmentDayBinding
    private lateinit var viewModel: MainActivityViewModel
    var currentStudy: Study? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_day,
            container,
            false
        )
        val application = requireNotNull(this.activity).application
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        val currentDate = LocalDateTime.now()
        val formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        Log.i("DayFragment", "Current Date is $formattedDate")

        viewModel.getCurrentStudySession(currentDate = formattedDate)


        viewModel.currentStudySession.observe(viewLifecycleOwner, Observer {
            it?.let {
                setBarChart(it)

            }
        })


        return binding.root
    }


    private fun setBarChart(study: Study) {
        val entries = ArrayList<BarEntry>()
        Log.i("DayFragment", " the study objects hours are ${study.hours}")
        Log.i("DayFragment", " the study objects minutes are ${study.minutes}")
        Log.i("DayFragment", " the study objects date are ${study.date}")
        Log.i("DayFragment", " the study objects dayOfMonth are ${study.dayOfMonth}")
//        entries.add(BarEntry(study.hours, 2)) //at what x index should we put this value of 8f
        entries.add(BarEntry(10F, 0))

        val barDataSet = BarDataSet(entries, "Cells")
        barDataSet.color = resources.getColor(R.color.colorAccent)


        val labels = ArrayList<String>()
        labels.add("")


        val barData = BarData(labels, barDataSet )

        binding.barChart.apply {
            xAxis.position = XAxis.XAxisPosition.TOP
            xAxis.spaceBetweenLabels = 0
            data = barData
            animateY(1000)
            fitScreen()
            axisLeft.setAxisMinValue(0F)

        }



    }

}