package hfad.com.studytimeapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.data.BarData
import hfad.com.studytimeapp.R
import hfad.com.studytimeapp.data.StudyDatabase
import hfad.com.studytimeapp.databinding.ActivityMonthDetailBinding
import hfad.com.studytimeapp.viewmodelfactories.MonthDetailFactory
import hfad.com.studytimeapp.viewmodels.MonthDetailViewModel

class MonthDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMonthDetailBinding
    private lateinit var viewModel: MonthDetailViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this , R.layout.activity_month_detail)

        binding.lifecycleOwner = this

        val monthSelected = intent.getIntExtra("month_selected", 0)

        val studyDao = StudyDatabase.getDatabase(application).studyDao()

        val viewModelFactory = MonthDetailFactory(monthSelected, application, studyDao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MonthDetailViewModel::class.java)

        viewModel.monthBarData.observe(this, Observer {
            it?.let {
                setBarChart(it)
            }
        })

    }

    private fun setBarChart(barData: BarData) {
        val xaxis = binding.activityMonthBarChart.xAxis //sets the spacing between the x labels
        xaxis.spaceBetweenLabels = 0

//        binding.monthBarChart.fitScreen()
        binding.activityMonthBarChart.data = barData // set the data and list of lables into chart
        binding.activityMonthBarChart.setDescription(viewModel.month)

//        //barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
//        barDataSet.color = resources.getColor(R.color.colorAccent)

        binding.activityMonthBarChart.animateY(2000)
    }
}