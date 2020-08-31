package hfad.com.studytimeapp.activities

import android.net.sip.SipSession
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import hfad.com.studytimeapp.R
import hfad.com.studytimeapp.adapters.MonthsWithSessionsAdapter
import hfad.com.studytimeapp.adapters.YearsWithStudySessionsAdapter
import hfad.com.studytimeapp.viewmodelfactories.SessionMonthSelectorViewModelFactory
import hfad.com.studytimeapp.viewmodels.SessionMonthSelectorViewModel
import java.time.LocalDateTime

class SessionMonthSelectorActivity : AppCompatActivity() {

    private lateinit var viewModel: SessionMonthSelectorViewModel
    private val currentYear = LocalDateTime.now().year


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session_month_selector)

        val viewModelFactory = SessionMonthSelectorViewModelFactory(currentYear, application)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(SessionMonthSelectorViewModel::class.java)

        val adapter = YearsWithStudySessionsAdapter() {

            Toast.makeText(this, "The item clicked is $it", Toast.LENGTH_LONG).show()
            viewModel.getMonthsWithSelectedYear(it)
        }//no list in constructor so we can manually set the list when data changes

        val monthAdapter = MonthsWithSessionsAdapter()

        viewModel.monthsFromSelectedYear.observe(this, Observer {
            it?.let {
                monthAdapter.setData(it)
            }
        })


        viewModel.yearsList.observe(this, Observer {
            it?.let {
                adapter.setData(it) //When the data changes (we get results from database) set the adapter's empty list to viiew model's list of years from database
            }
        })

        val monthRecyclerView = findViewById<RecyclerView>(R.id.month_recycler_view)
        monthRecyclerView.adapter = monthAdapter

        val recyclerView = findViewById<RecyclerView>(R.id.yearsListRecyclerView)
        recyclerView.adapter = adapter


    }


}