package hfad.com.studytimeapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import hfad.com.studytimeapp.R
import hfad.com.studytimeapp.data.Study
import hfad.com.studytimeapp.databinding.ActivityMainBinding
import hfad.com.studytimeapp.fragments.MonthViewFragment
import hfad.com.studytimeapp.fragments.WeekFragment
import hfad.com.studytimeapp.viewmodels.MainActivityViewModel
import java.time.LocalDateTime


class MainActivity : FragmentActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewmodel: MainActivityViewModel
    private var currentMonth = 0
    private var currentDayOfMonth = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        currentMonth = LocalDateTime.now().monthValue
        currentDayOfMonth = LocalDateTime.now().dayOfMonth


         viewmodel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        val studySession1 = Study(
            hours = 30F,
            minutes = 60,
            date = "2020-08-28",
            weekDay = "MONDAY",
            dayOfMonth = 28,
            month = 11

        )

        supportFragmentManager.commit {
            add<WeekFragment>(R.id.fragment_container, null)
        }


        val currentDayOfMonth = LocalDateTime.now().dayOfMonth
        val currentMonth = LocalDateTime.now().monthValue

//        viewmodel.insertStudySession()



        binding.weekChip.setOnClickListener {
            supportFragmentManager.commit {
                replace<WeekFragment>(R.id.fragment_container, null)
                addToBackStack(null)
            }
        }


        binding.monthChip.setOnClickListener {
            supportFragmentManager.commit {
                replace<MonthViewFragment>(R.id.fragment_container, null)
                addToBackStack(null)
            }
        }
        binding.addSessionFab.setOnClickListener {
            val intent = Intent(this, TimerActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onResume() {
        super.onResume()
        viewmodel.setLastSevenStudySessionsData(currentMonth, currentDayOfMonth)
        viewmodel.setAllSelectedMonthData(currentMonth)

    }


}


