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
import kotlin.time.hours

class MainActivityViewModel(application: Application) : AndroidViewModel(application){

    val viewModelJob = Job()
    val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

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


    private val _sessionsWithMatchingMonth = MutableLiveData<MutableList<Study>>()
    private val _lastSevenStudySessionHours = MutableLiveData<List<Study>>()
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
            _sessionsWithMatchingMonth.value = repository.getAllSessionsWithMatchingMonth(seletedMonth)


            val entries = MutableList(31){BarEntry(0F, 0)}

            if(_sessionsWithMatchingMonth.value.isNullOrEmpty()){
                val barDataSet = BarDataSet(entries, "Hours")
                _monthBarData.value = BarData(monthDayLabels, barDataSet)

            }else{
                //Entries uses the prefixed size so we can add values to it add specific indexes
                //BarEntry(value, index) we can specify the index this bar value will be placed

                for(i in 0 until  _sessionsWithMatchingMonth.value!!.size){
                    entries[_sessionsWithMatchingMonth.value!![i].dayOfMonth] = BarEntry(_sessionsWithMatchingMonth.value!![i].hours, _sessionsWithMatchingMonth.value!![i].dayOfMonth - 1) //to match the array indexes
                }

                val barDataSet = BarDataSet(entries, "Hours")
                month = months[_sessionsWithMatchingMonth.value!![0].month - 1] //set the month value

                _monthBarData.value = BarData(monthDayLabels, barDataSet)
            }

        }
    }

    fun setLastSevenStudySessionsData(currentMonth: Int, currentDayOfMonth: Int){
        viewModelScope.launch {
            _lastSevenStudySessionHours.value = repository.getLastSevenSessions(currentMonth, currentDayOfMonth)

            val entries = ArrayList<BarEntry>()


            if(_lastSevenStudySessionHours.value.isNullOrEmpty()){
                val barDataSet = BarDataSet(entries, "Sessions")
                _weekBarData.value = BarData(nullLabels, barDataSet)

            }else{
                val datesFromSessions = ArrayList<String>()

                for(session in _lastSevenStudySessionHours.value!!.indices){
                    entries.add(BarEntry(_lastSevenStudySessionHours.value!![session].hours, session))
                    datesFromSessions.add(_lastSevenStudySessionHours.value!![session].date)
                }

                //check if entries is null, if not loop through entries check the day of the week

//            val dataLabels = arrayListOf<String>()
//
//            for(i in _lastSevenStudySessionHours.value!!.indices){
//                dataLabels.add(_lastSevenStudySessionHours.value!![i].date)
//            }

                val barDataSet = BarDataSet(entries, "Cells")

                _weekBarData.value = BarData(datesFromSessions, barDataSet)
            }

        }
    }



//    Inserts mock study session from MainActivity - already inserted into database though
    fun insertStudySession(){

        viewModelScope.launch {


            for (day in 0 until 7) {


                var study = Study(
                    hours = day.toFloat(),
                    minutes = 80,
                    date = "2020-08-${day+1}",
                    weekDay = "WEDNESDAY",
                    month = 11,
                    dayOfMonth = day + 1

                )
                repository.insertStudySession(study)

            }
        }
    }

//    fun insertStudySession(){
//
//        viewModelScope.launch {
//
//
//
//
//                var study = Study(
//                    hours = 60F,
//                    minutes = 80,
//                    date = "2020-08-2",
//                    weekDay = "WEDNESDAY",
//                    month = 11,
//                    dayOfMonth = 2
//
//                )
//
//            var study2 = Study(
//                hours = 60F,
//                minutes = 80,
//                date = "2020-08-5",
//                weekDay = "FRIDAY",
//                month = 11,
//                dayOfMonth = 5
//
//            )
//                repository.insertStudySession(study2)
//
//
//        }
//    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}