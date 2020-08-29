package hfad.com.studytimeapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import hfad.com.studytimeapp.R
import hfad.com.studytimeapp.databinding.FragmentWeekBinding
import hfad.com.studytimeapp.viewmodels.MainActivityViewModel
import hfad.com.studytimeapp.viewmodels.WeekViewModel
import java.time.LocalDateTime

/*
    As of right now the Each Fragment calls is setMonth, and setWeek view model functions which query database for each.
    This is taking a minute to display becauase we update the view when there is a change in this data (when the database query returns)

    Lets try to call these methods from Main activity
 */
class WeekFragment : Fragment() {

    private lateinit var binding: FragmentWeekBinding
    private val viewModel: MainActivityViewModel by activityViewModels()

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

        viewModel.weekBarData.observe(viewLifecycleOwner, Observer {
            it?.let {
                setBarChart(it)
            }
        })

        return binding.root
    }



    private fun setBarChart(barData: BarData) {
        binding.weekBarChart.fitScreen()
        binding.weekBarChart.data = barData // set the data and list of lables into chart
        binding.weekBarChart.setDescription("Sessions from last 7 days")

//        //barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
//        barDataSet.color = resources.getColor(R.color.colorAccent)

        binding.weekBarChart.animateY(2000)
    }


}