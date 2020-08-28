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

    val viewModelJob = Job()
    val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val studyDao = StudyDatabase.getDatabase(application, viewModelScope).studyDao()
    private val repository = StudyRepository(studyDao)
    private val weekDays = arrayListOf<String>(
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" , "Saturday", "Sunday"
    )
    private val monthDayLabels = arrayListOf<String>("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31")
    private val weekDayLabels = arrayListOf<String>("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")


    //MainActivity will observe this list - You will need to sort this list
    private val _lastSevenSessions = MutableLiveData<List<Study>>()
    val lastSevenSessions: LiveData<List<Study>>
    get() = _lastSevenSessions

    private val _currentStudySession = MutableLiveData<Study>()
    val currentStudySession: LiveData<Study>
    get() = _currentStudySession


    private val _weekBarData = MutableLiveData<BarData>()
    val weekBarData: LiveData<BarData>
        get() = _weekBarData


    private val _monthBarData = MutableLiveData<BarData>()
    val monthBarData: LiveData<BarData>
        get() = _monthBarData


    private val _sessionsWithMatchingMonth = MutableLiveData<List<Study>>()
    private val _lastSevenStudySessionHours = MutableLiveData<List<Study>>()



    fun upsertStudySession(newStudySession: Study){
        viewModelScope.launch {
            repository.insertStudySession(newStudySession)
        }
    }

    fun getCurrentStudySession(currentDate: String){
        viewModelScope.launch {
            _currentStudySession.value = repository.getCurrentStudySession(currentDate)
        }
    }


    fun setAllSelectedMonthData(seletedMonth: Int){
        viewModelScope.launch {
            _sessionsWithMatchingMonth.value = repository.getAllSessionsWithMatchingMonth(seletedMonth)
            val entries = ArrayList<BarEntry>()

            for(session in 0 until 31){
                entries.add(BarEntry(_sessionsWithMatchingMonth.value!![session].hours, session))
            }

            val barDataSet = BarDataSet(entries, "Cells")

//            val labels = ArrayList<String>()
//            for(i in 0 until 32){
//                labels.add(i.toString())
//            }

            _monthBarData.value = BarData(monthDayLabels, barDataSet)

        }
    }

    fun setLastSevenStudySessionsData(currentMonth: Int, currentDayOfMonth: Int){
        viewModelScope.launch {
            _lastSevenStudySessionHours.value = repository.getLastSevenSessions(currentMonth, currentDayOfMonth)

            val entries = ArrayList<BarEntry>()

            for(session in _lastSevenStudySessionHours.value!!.indices){
                entries.add(BarEntry(_lastSevenStudySessionHours.value!![session].hours, session))
            }

            val barDataSet = BarDataSet(entries, "Cells")

            _weekBarData.value = BarData(weekDayLabels, barDataSet)
        }
    }



    //Inserts mock study session from MainActivity - already inserted into database though
    fun insertStudySession(){

        viewModelScope.launch {


            for (day in 0 until 31) {



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

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}