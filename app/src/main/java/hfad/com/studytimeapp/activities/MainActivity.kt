package hfad.com.studytimeapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import hfad.com.studytimeapp.R
import hfad.com.studytimeapp.databinding.ActivityMainBinding
import hfad.com.studytimeapp.fragments.DayFragment
import hfad.com.studytimeapp.fragments.WeekFragment


class MainActivity : FragmentActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this



        supportFragmentManager.commit {
            add<DayFragment>(R.id.fragment_container, null)
        }

        binding.weekChip.setOnClickListener {
            supportFragmentManager.commit {
                replace<WeekFragment>(R.id.fragment_container, null)
                addToBackStack(null)
            }
        }

        binding.dayChip.setOnClickListener {
            supportFragmentManager.commit {
                replace<DayFragment>(R.id.fragment_container, null)
                addToBackStack(null)
            }
        }
        binding.studyChip.setOnClickListener {
            val intent = Intent(this, TimerActivity::class.java)
            startActivity(intent)
        }


    }


}


