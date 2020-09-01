package hfad.com.studytimeapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import hfad.com.studytimeapp.data.Study
import hfad.com.studytimeapp.data.StudyDatabase
import hfad.com.studytimeapp.data.StudyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivityViewModel(application: Application, currentMonth: Int, currentDayOfMonth: Int) : AndroidViewModel(application){

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob) //coroutine scope keeps track of coroutines launch coroutines inside of coroutine scopes

    private val studyDao = StudyDatabase.getDatabase(application, viewModelScope).studyDao()
    private val repository = StudyRepository(studyDao)
    private val weekDays = arrayListOf<String>(
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" , "Saturday", "Sunday"
    )
    private val monthDayLabels = arrayListOf<String>("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31")
    private val weekDayLabels = arrayListOf<String>("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    private val nullLabels = arrayListOf<String>("No Data", "No Data", "No Data", "No Data", "No Data", "No Data", "No Data")
    private val months = arrayListOf<String>("January", "February" ,"March", "April", "May", "June", "July", "August", "September", "October", "November", "December")


    private val _weekBarData = MutableLiveData<BarData>()
    val weekBarData: LiveData<BarData>
        get() = _weekBarData


    private val _monthBarData = MutableLiveData<BarData>()
    val monthBarData: LiveData<BarData>
        get() = _monthBarData


    private val _allSessionsWithMatchingMonth = MutableLiveData<List<Study>>()
    var allSessionsWithMatchingMonth: LiveData<List<Study>> = _allSessionsWithMatchingMonth
    
    
    private val _lastSevenStudySessions = MutableLiveData<List<Study>>()
    var lastSevenStudySessions: LiveData<List<Study>> = _lastSevenStudySessions
    
    init {
        getAllSessionsWithMatchingMonth(currentMonth)
        getLastSevenStudySessions(currentMonth, currentDayOfMonth)
        
    }
    
    
    var month: String = ""

    fun upsertStudySession(newStudySession: Study){
        viewModelScope.launch {
            repository.insertStudySession(newStudySession)
        }
    }

    /*
        Entries must be fixed size in order to set specfic index values. We set its values to emtpy BarEntries.
        Then loop for the size of the study sessions array we get back from the databas query
        we get the entry index from the ith study sessions day of month and set its value to the ith sessions hours and set the index as well
     */
    fun getAllSessionsWithMatchingMonth(seletedMonth: Int){
        viewModelScope.launch {
            _allSessionsWithMatchingMonth.value = repository.getAllSessionsWithMatchingMonth(seletedMonth)
            setSessionWithMonthBarData()
            
        }
    }


    fun setSessionWithMonthBarData() {

        val monthBarDataSetValues = MutableList(31) { BarEntry(0F, 0) }

        if (_allSessionsWithMatchingMonth.value.isNullOrEmpty()) {
            val barDataSet = BarDataSet(monthBarDataSetValues, "Hours")
            _monthBarData.value = BarData(monthDayLabels, barDataSet)

        } else {
            //Entries uses the fixed size so we can add values to it at specific indexes
            //BarEntry(value, index) we can specify the index this bar value will be placed

            for (i in 0 until _allSessionsWithMatchingMonth.value!!.size) {
                monthBarDataSetValues[_allSessionsWithMatchingMonth.value!![i].dayOfMonth - 1] =
                    BarEntry(
                        _allSessionsWithMatchingMonth.value!![i].hours,
                        _allSessionsWithMatchingMonth.value!![i].dayOfMonth - 1
                    ) //to match the array indexes
            }

            val monthBarDataSet = BarDataSet(monthBarDataSetValues, "Hours")
            month =
                months[_allSessionsWithMatchingMonth.value!![0].month - 1] //set the month value to be displayed in the monthBarChart's description

            _monthBarData.value = BarData(monthDayLabels, monthBarDataSet)
        }
    }
    

    fun getLastSevenStudySessions(currentMonth: Int, currentDayOfMonth: Int){
        viewModelScope.launch {
            _lastSevenStudySessions.value =
                repository.getLastSevenSessions(currentMonth, currentDayOfMonth)
            setLastSevenSessionsBarData()
        }
    }


        fun setLastSevenSessionsBarData() {
            val weekBarDataSetValues = ArrayList<BarEntry>()

            if (_lastSevenStudySessions.value.isNullOrEmpty()) {
                val barDataSet = BarDataSet(weekBarDataSetValues, "Sessions")
                _weekBarData.value = BarData(nullLabels, barDataSet)

            } else {
                val datesFromSessions = ArrayList<String>()

                for (session in _lastSevenStudySessions.value!!.indices) {
                    weekBarDataSetValues.add(
                        BarEntry(
                            _lastSevenStudySessions.value!![session].hours,
                            session
                        )
                    ) //
                    datesFromSessions.add(_lastSevenStudySessions.value!![session].date)
                }

                val weekBarDataSet = BarDataSet(weekBarDataSetValues, "Cells")

                _weekBarData.value = BarData(datesFromSessions, weekBarDataSet)
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
                hours = 10F,
                minutes = 20,
                date = "2019-09-27",
                weekDay = "MONDAY",
                month = 9,
                year = 2019,
                dayOfMonth = 27
            )

            repository.insertStudySession(study)
        }
    }

    override fun onCleared() { //if view model is destroyed all coroutines within the job are canceled - prevents work leaks
        super.onCleared()
        viewModelJob.cancel()
    }


}