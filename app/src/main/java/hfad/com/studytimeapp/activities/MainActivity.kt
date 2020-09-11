package hfad.com.studytimeapp.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginEnd
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import hfad.com.studytimeapp.R
import hfad.com.studytimeapp.data.StudyDatabase
import hfad.com.studytimeapp.databinding.ActivityMainBinding
import hfad.com.studytimeapp.fragments.MonthViewFragment
import hfad.com.studytimeapp.fragments.WeekFragment
import hfad.com.studytimeapp.viewmodelfactories.MainViewModelFactory
import hfad.com.studytimeapp.viewmodels.MainActivityViewModel
import java.time.LocalDateTime


class MainActivity :  AppCompatActivity(){

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

        val studyDao = StudyDatabase.getDatabase(application).studyDao()

        val viewModelFactory = MainViewModelFactory(application, studyDao, currentMonth, currentDayOfMonth)
        viewmodel = ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java) //on init gets list of week and month sessions, sets week data and month data

//        viewmodel.insertAStudySession()

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
        binding.addSessionChip.setOnClickListener {
            val intent = Intent(this, TimerActivity::class.java)
            startActivity(intent)
        }

        binding.sessionsChip.setOnClickListener {
            val intent = Intent(this, SessionMonthSelectorActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("display", displayWeekFragment)
    }




    /*
         have to call these methods in onResume in order to get observers (week and month fragment) to update their views.
        When the use enters a study session for the same day the hours change but the list of lastSevenSessions does not.
        To get the bar data to update we have to call these methods to make observers be able to update the new hours.
     */
//    override fun onResume() {
//        super.onResume()
//        viewmodel.getLastSevenStudySessions(currentMonth, currentDayOfMonth)
//        viewmodel.getAllSessionsWithMatchingMonth(currentMonth)
//    }
}


