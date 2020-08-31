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

class MainActivityViewModel(application: Application) : AndroidViewModel(application){

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


    private val allSessionWithMatchingMonth = MutableLiveData<MutableList<Study>>()
    private val lastSevenStudySessions = MutableLiveData<List<Study>>()
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
    fun setAllSelectedMonthData(seletedMonth: Int){
        viewModelScope.launch {
            allSessionWithMatchingMonth.value = repository.getAllSessionsWithMatchingMonth(seletedMonth)

            val monthBarDataSetValues = MutableList(31){BarEntry(0F, 0)}

            if(allSessionWithMatchingMonth.value.isNullOrEmpty()){
                val barDataSet = BarDataSet(monthBarDataSetValues, "Hours")
                _monthBarData.value = BarData(monthDayLabels, barDataSet)

            }else{
                //Entries uses the fixed size so we can add values to it at specific indexes
                //BarEntry(value, index) we can specify the index this bar value will be placed

                for(i in 0 until  allSessionWithMatchingMonth.value!!.size){
                    monthBarDataSetValues[allSessionWithMatchingMonth.value!![i].dayOfMonth - 1] = BarEntry(allSessionWithMatchingMonth.value!![i].hours, allSessionWithMatchingMonth.value!![i].dayOfMonth - 1) //to match the array indexes
                }

                val monthBarDataSet = BarDataSet(monthBarDataSetValues, "Hours")
                month = months[allSessionWithMatchingMonth.value!![0].month - 1] //set the month value to be displayed in the monthBarChart's description

                _monthBarData.value = BarData(monthDayLabels, monthBarDataSet)
            }
        }
    }

    fun setLastSevenStudySessionsData(currentMonth: Int, currentDayOfMonth: Int){
        viewModelScope.launch {
            lastSevenStudySessions.value = repository.getLastSevenSessions(currentMonth, currentDayOfMonth)

            val weekBarDataSetValues = ArrayList<BarEntry>()

            if(lastSevenStudySessions.value.isNullOrEmpty()){
                val barDataSet = BarDataSet(weekBarDataSetValues, "Sessions")
                _weekBarData.value = BarData(nullLabels, barDataSet)

            }else{
                val datesFromSessions = ArrayList<String>()

                for(session in lastSevenStudySessions.value!!.indices){
                    weekBarDataSetValues.add(BarEntry(lastSevenStudySessions.value!![session].hours, session)) //
                    datesFromSessions.add(lastSevenStudySessions.value!![session].date)
                }

                val weekBarDataSet = BarDataSet(weekBarDataSetValues, "Cells")

                _weekBarData.value = BarData(datesFromSessions, weekBarDataSet)
            }
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
                date = "2019-08-27",
                weekDay = "MONDAY",
                month = 8,
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