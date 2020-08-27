package hfad.com.studytimeapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import hfad.com.studytimeapp.R
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

//        val monthViewModel = ViewModelProvider(this).get(MonthViewModel::class.java)
//        val weekViewModel = ViewModelProvider(this).get(WeekViewModel::class.java)

        val viewmodel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
//
        val currentDayOfMonth = LocalDateTime.now().dayOfMonth
        val currentMonth = LocalDateTime.now().monthValue

//        viewModel.setLastSevenStudySessionsData(currentMonth, currentDayOfMonth)
//        viewModel.setAllSelectedMonthData(currentMonth)


//        monthViewModel.setAllSessionsWithMatchingMonthData(currentMonth)
//        weekViewModel.setLastSevenStudySessions(currentMonth, currentDayOfMonth)


//        viewModel.setAllSelectedMonthData(currentMonth)
//        viewModel.setLastSevenStudySessionsData(currentMonth, currentDayOfMonth)



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


