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
import hfad.com.studytimeapp.viewmodelfactories.MainViewModelFactory
import hfad.com.studytimeapp.viewmodels.MainActivityViewModel
import java.time.LocalDateTime


class MainActivity : FragmentActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewmodel: MainActivityViewModel
    private var currentMonth = 0
    private var currentDayOfMonth = 0
    private var displayWeekFragment = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        currentMonth = LocalDateTime.now().monthValue
        currentDayOfMonth = LocalDateTime.now().dayOfMonth


        val viewModelFactory = MainViewModelFactory(application, currentMonth, currentDayOfMonth)
        viewmodel = ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java) //on init gets list of week and month sessions, sets week data and month data



        if (savedInstanceState != null) {
            if(!savedInstanceState.getBoolean("display")){
                supportFragmentManager.commit {
                    replace<MonthViewFragment>(R.id.fragment_container, null)
                    displayWeekFragment = true
                }
            }
        }else{
            supportFragmentManager.commit {
                replace<WeekFragment>(R.id.fragment_container, null)
                displayWeekFragment = true
            }
        }

        binding.weekChip.setOnClickListener {
            supportFragmentManager.commit {
                replace<WeekFragment>(R.id.fragment_container, null)
                addToBackStack(null)
                displayWeekFragment = true
            }
        }

        binding.monthChip.setOnClickListener {
            supportFragmentManager.commit {
                replace<MonthViewFragment>(R.id.fragment_container, null)
                addToBackStack(null)
                displayWeekFragment = false
            }
        }
        binding.addSessionFAB.setOnClickListener {
            val intent = Intent(this, TimerActivity::class.java)
            startActivity(intent)
        }

        binding.btnGoToSessionView.setOnClickListener {
            val intent = Intent(this, SessionMonthSelectorActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("display", displayWeekFragment)
    }

//    override fun onResume() {
//        super.onResume()
//        viewmodel.getLastSevenStudySessions(currentMonth, currentDayOfMonth)
//        viewmodel.getAllSessionsWithMatchingMonth(currentMonth)
//    }
}


