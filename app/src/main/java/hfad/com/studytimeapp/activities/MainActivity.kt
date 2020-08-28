package hfad.com.studytimeapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this


        val viewmodel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        val studySession1 = Study(
            hours = 1F,
            minutes = 60,
            date = "2020-08-28",
            weekDay = "MONDAY",
            dayOfMonth = 28,
            month = 8

        )
        val studySession2 = Study(
            hours = 2F,
            minutes = 120,
            date = "2020-08-28",
            weekDay = "MONDAY",
            dayOfMonth = 28,
            month = 8

        )
        val studySession3 = Study(
            hours = 3F,
            minutes = 180,
            date = "2020-08-29",
            weekDay = "MONDAY",
            dayOfMonth = 29,
            month = 8

        )
        val studySession4 = Study(
            hours = 1F,
            minutes = 240,
            date = "2020-08-29",
            weekDay = "MONDAY",
            dayOfMonth = 29,
            month = 8

        )



//        viewmodel.insertStudySession(studySession1)
//        viewmodel.insertStudySession(studySession2)
//        viewmodel.insertStudySession(studySession3)
        viewmodel.updateStudySession(8, 29, 10F)


//        viewmodel.insertStudySession()

//        viewmodel.setAllSelectedMonthData(8)

        val currentDayOfMonth = LocalDateTime.now().dayOfMonth
        val currentMonth = LocalDateTime.now().monthValue

        Log.i("MainActivity", "currentDayOfMonth is $currentDayOfMonth current month is $currentMonth")

//        viewmodel.setLastSevenStudySessionsData(currentMonth, currentDayOfMonth)


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
        binding.studyChip.setOnClickListener {
            val intent = Intent(this, TimerActivity::class.java)
            startActivity(intent)
        }


    }


}


