package hfad.com.studytimeapp.activities

import android.inputmethodservice.Keyboard
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.text.format.DateFormat.format
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.postDelayed
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import hfad.com.studytimeapp.R
import hfad.com.studytimeapp.data.Study
import hfad.com.studytimeapp.databinding.ActivityTimerBinding
import hfad.com.studytimeapp.viewmodels.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_timer.*
import java.lang.String.format
import java.sql.Time
import java.text.DateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

class TimerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimerBinding
    private lateinit var viewModel: MainActivityViewModel
    private var date =  Date()

    var START_MILLI_SECONDS = 0L
     var countdown_timer: CountDownTimer? = null
    var isRunning: Boolean = false;
    var time_in_milli_seconds = 0L
    var time_in_hours = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_timer)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        binding.startButton.setOnClickListener {

            if (isRunning) {
                pauseTimer()
            } else {
                val time  = etTimeInput.text.toString()
                time_in_hours = time.toLong()
                time_in_milli_seconds = TimeUnit.HOURS.toMillis(time_in_hours)

                startTimer(time_in_milli_seconds)
            }
        }


        //Adding a study session needs to insert into database
        binding.addStudySessionChip.setOnClickListener {
            if(binding.etTimeInput.text.isNullOrEmpty()){
                Toast.makeText(this, "Please enter a study duration", Toast.LENGTH_SHORT).show()
            }else{
                binding.startButton.text = "start"
                countdown_timer?.cancel()
                val minutesStudied = TimeUnit.MILLISECONDS.toMinutes(time_in_milli_seconds)
                val hoursStudied = (minutesStudied/60.0).toFloat()

                val currentDayOfMonth = LocalDateTime.now().dayOfMonth
                val currentMonth = LocalDateTime.now().monthValue
                val currentWeekDay = LocalDateTime.now().dayOfWeek
                val currentDate = LocalDateTime.now()

                val formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))


                val studySession = Study(
                    hours = hoursStudied,
                    minutes = minutesStudied,
                    date = currentDate.toString(),
                    weekDay = currentWeekDay.toString(),
                    month = currentMonth,
                    dayOfMonth = currentDayOfMonth
                )

                viewModel.upsertStudySession(studySession)


            }
        }
        binding.btnReset.setOnClickListener {
            resetTimer()
        }
    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("savedMiliseconds", time_in_milli_seconds)
        Toast.makeText(this, "saved milli seconds is $time_in_milli_seconds", Toast.LENGTH_LONG).show()
    }


    private fun pauseTimer() {
        startButton.text = "Start"
        countdown_timer?.cancel()
        isRunning = false
    }

    private fun startTimer(time_in_seconds: Long) {
        countdown_timer = object : CountDownTimer(time_in_seconds, 1000) {
            override fun onFinish() {

            }

            override fun onTick(p0: Long) {
                time_in_milli_seconds = p0
                updateTextUI()
            }
        }
        countdown_timer?.start()
        isRunning = true
        startButton.text = "Pause"
    }

    private fun resetTimer() {
        binding.startButton.text = "start"
        countdown_timer?.cancel()
        time_in_milli_seconds = START_MILLI_SECONDS
        updateTextUI()
    }

    private fun updateTextUI() {

        val time = String.format("%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(time_in_milli_seconds),
            TimeUnit.MILLISECONDS.toMinutes(time_in_milli_seconds) -
                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time_in_milli_seconds)), // The change is in this line
            TimeUnit.MILLISECONDS.toSeconds(time_in_milli_seconds) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time_in_milli_seconds)));

        tvTimerCountDown.text = "$time"
    }



}