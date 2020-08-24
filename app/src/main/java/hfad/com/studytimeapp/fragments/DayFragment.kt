package hfad.com.studytimeapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import hfad.com.studytimeapp.R
import hfad.com.studytimeapp.databinding.FragmentDayBinding
import kotlinx.android.synthetic.main.fragment_day.*


class DayFragment : Fragment() {

    private lateinit var binding: FragmentDayBinding


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

        setBarChart()

        return binding.root
    }


    private fun setBarChart() {
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(20f, 0))   //at what x index should we put this value of 8f
        entries.add(BarEntry(20f, 1))
        entries.add(BarEntry(50f, 2))
        entries.add(BarEntry(20f, 3))
        entries.add(BarEntry(15f, 4))
        entries.add(BarEntry(60F, 5))
        entries.add(BarEntry(30F, 7))
        entries.add(BarEntry(10F, 8))
        entries.add(BarEntry(12F, 9))
        entries.add(BarEntry(14F, 10))
        entries.add(BarEntry(16F, 11))
        entries.add(BarEntry(45F, 12))

        val barDataSet = BarDataSet(entries, "Cells")

        val labels = ArrayList<String>()
        labels.add("6am")
        labels.add("7am")
        labels.add("8am")
        labels.add("9am")
        labels.add("10am")
        labels.add("11 am")
        labels.add("12 pm")
        labels.add("1 pm")
        labels.add("2pm")
        labels.add("3pm")
        labels.add("4pm")
        labels.add("5pm")

        val xaxis = binding.barChart.xAxis
        xaxis.spaceBetweenLabels = 1

        val data = BarData(labels, barDataSet)
        binding.barChart.data = data // set the data and list of lables into chart


        //barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
        barDataSet.color = resources.getColor(R.color.colorAccent)

        binding.barChart.animateY(5000)
    }

}