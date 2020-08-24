package hfad.com.studytimeapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import hfad.com.studytimeapp.R
import hfad.com.studytimeapp.databinding.FragmentWeekBinding


class WeekFragment : Fragment() {

    private lateinit var binding: FragmentWeekBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_week,
            container,
            false
        )

        setBarChart()

        return binding.root
    }


    private fun setBarChart() {
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(8f, 0))   //at what x index should we put this value of 8f
        entries.add(BarEntry(2f, 1))
        entries.add(BarEntry(5f, 2))
        entries.add(BarEntry(20f, 3))
        entries.add(BarEntry(15f, 4))
        entries.add(BarEntry(100F, 5))
        entries.add(BarEntry(100F, 6))

        val barDataSet = BarDataSet(entries, "Cells")

        val labels = ArrayList<String>()
        labels.add("Monday")
        labels.add("Tuesday")
        labels.add("Wednesday")
        labels.add("Thursday")
        labels.add("Friday")
        labels.add("Saturday")
        labels.add("Sunday")

        val xaxis = binding.weekBarChart.xAxis //sets the spacing between the x labels
        xaxis.spaceBetweenLabels = 0
//        xaxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
//        binding.weekBarChart.setExtraOffsets(5f, 5f,  5f, 5f)
//

        binding.weekBarChart.fitScreen()
        val data = BarData(labels, barDataSet)
        binding.weekBarChart.data = data // set the data and list of lables into chart


        //barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
        barDataSet.color = resources.getColor(R.color.colorAccent)

        binding.weekBarChart.animateY(5000)
    }

}