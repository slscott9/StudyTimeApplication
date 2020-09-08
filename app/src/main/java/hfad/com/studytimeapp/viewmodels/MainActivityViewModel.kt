package hfad.com.studytimeapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import hfad.com.studytimeapp.data.Study
import hfad.com.studytimeapp.data.StudyDao
import hfad.com.studytimeapp.data.StudyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivityViewModel(application: Application, private val studyDao: StudyDao, currentMonth: Int, currentDayOfMonth: Int) : AndroidViewModel(application){

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob) //coroutine scope keeps track of coroutines launch coroutines inside of coroutine scopes

    private val repository = StudyRepository(studyDao)
    private val weekDays = arrayListOf<String>(
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" , "Saturday", "Sunday"
    )
    private val monthDayLabels = arrayListOf<String>("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31")
    private val weekDayLabels = arrayListOf<String>("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    private val nullLabels = arrayListOf<String>("No Data", "No Data", "No Data", "No Data", "No Data", "No Data", "No Data")
    private val months = arrayListOf<String>("January", "February" ,"March", "April", "May", "June", "July", "August", "September", "October", "November", "December")

    var month: String = ""

    /*
        When _lastSevenSessionsHours changes get the last sevenStudySessions, use date and hours properties to transform into bar data.

        Activities observe bar data and update bar charts
     */
    private val _lastSevenSessionsHours = repository.getLastSevenSessionsHours(currentMonth, currentDayOfMonth)
    private val lastSevenStudySessions : LiveData<List<Study>> = Transformations.switchMap(_lastSevenSessionsHours){
        repository.getLastSevenSessions(currentMonth, currentDayOfMonth)
    }
    val weekBarData: LiveData<BarData> = Transformations.map(lastSevenStudySessions){
        setLastSevenSessionsBarData(it)
    }

    private val _monthsStudySessionHours = repository.getSessionHoursForMonth(currentMonth)
    private val _monthsStudySessions = Transformations.switchMap(_monthsStudySessionHours){
        repository.getAllSessionsWithMatchingMonth(currentMonth)
    }
    val monthBarData = Transformations.map(_monthsStudySessions){
        setSessionWithMonthBarData(it)
    }

    fun setSessionWithMonthBarData(monthsStudySessionList: List<Study>) : BarData{

        val monthBarDataSetValues = MutableList(31) { BarEntry(0F, 0) }
        var monthBarData = BarData()

        if (monthsStudySessionList.isNullOrEmpty()) {
            val barDataSet = BarDataSet(monthBarDataSetValues, "Hours")
            monthBarData = BarData(monthDayLabels, barDataSet)

        } else {
            //Entries uses the fixed size so we can add values to it at specific indexes
            //BarEntry(value, index) we can specify the index this bar value will be placed

            for (i in monthsStudySessionList.indices) {
                monthBarDataSetValues[monthsStudySessionList[i].dayOfMonth - 1] =
                    BarEntry(
                        monthsStudySessionList[i].hours,
                        monthsStudySessionList[i].dayOfMonth - 1
                    ) //to match the array indexes
            }

            val monthBarDataSet = BarDataSet(monthBarDataSetValues, "Hours")
            month =
                months[monthsStudySessionList[0].month - 1] //set the month value to be displayed in the monthBarChart's description

            monthBarData = BarData(monthDayLabels, monthBarDataSet)
        }

        return monthBarData
    }


    fun setLastSevenSessionsBarData(list: List<Study>): BarData{
        val weekBarDataSetValues = ArrayList<BarEntry>()
        var weekBarData = BarData()

        if (list.isNullOrEmpty()) {
            val barDataSet = BarDataSet(weekBarDataSetValues, "Sessions")
            weekBarData = BarData(nullLabels, barDataSet)

        } else {
            val datesFromSessions = ArrayList<String>()

            for (session in list.indices) {
                weekBarDataSetValues.add(
                    BarEntry(
                        list[session].hours,
                        session
                    )
                )
                datesFromSessions.add(list[session].date)
            }
            val weekBarDataSet = BarDataSet(weekBarDataSetValues, "Hours")
            weekBarData = BarData(datesFromSessions, weekBarDataSet)
        }

        return weekBarData
    }


    fun upsertStudySession(newStudySession: Study){
        viewModelScope.launch {
            repository.insertStudySession(newStudySession)
        }
    }


//    Inserts mock study session from MainActivity - already inserted into database though
    fun insertStudySession(){
        viewModelScope.launch {

            for (day in 0 until 31 step 2 ) {

                var study = Study(
                    hours = day.toFloat(),
                    minutes = 80,
                    date = "2020-08-${day}",
                    weekDay = "WEDNESDAY",
                    month = 8,
                    dayOfMonth = day,
                    year = 0

                )
                repository.insertStudySession(study)
            }
        }
    }

    fun insertAStudySession(){
        viewModelScope.launch {
            val study = Study(
                hours = 3F,
                minutes = 20,
                date = "2019-09-27",
                weekDay = "MONDAY",
                month = 9,
                year = 2020,
                dayOfMonth = 27
            )

            val study1 = Study(
                hours = 14F,
                minutes = 20,
                date = "2019-09-2",
                weekDay = "MONDAY",
                month = 9,
                year = 2019,
                dayOfMonth = 2
            )
            val study2 = Study(
            hours = 17F,
            minutes = 20,
            date = "2019-01-7",
            weekDay = "MONDAY",
            month = 1,
            year = 2019,
            dayOfMonth = 7
        )
            val study3 = Study(
            hours = 10F,
            minutes = 20,
            date = "2019-08-27",
            weekDay = "MONDAY",
            month = 8,
            year = 2019,
            dayOfMonth = 27
        )
            val study4 = Study(
            hours = 10F,
            minutes = 20,
            date = "2018-03-7",
            weekDay = "MONDAY",
            month = 3,
            year = 2018,
            dayOfMonth = 7
        )


            repository.insertStudySession(study)
//            repository.insertStudySession(study1)
//            repository.insertStudySession(study2)
//            repository.insertStudySession(study3)
//            repository.insertStudySession(study4)
        }
    }

    override fun onCleared() { //if view model is destroyed all coroutines within the job are canceled - prevents work leaks
        super.onCleared()
        viewModelJob.cancel()
    }


}