package hfad.com.studytimeapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

class MainActivityViewModel(application: Application) : ViewModel(){

    val viewModelJob = Job()
    val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val studyDao = StudyDatabase.getDatabase(application, viewModelScope).studyDao()
    private val repository = StudyRepository(studyDao)


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
    private val _lastSevenStudySession = MutableLiveData<List<Study>>()



//    fun getLastSevenSessions(currentMonth: Int, currentDayOfMonth: Int){
//        viewModelScope.launch {
//            _lastSevenSessions.value = repository.getLastSevenSessions(currentMonth, currentDayOfMonth)
//        }
//    }

    fun insertStudySession(study: Study){
        viewModelScope.launch {
            repository.insertStudySession(study)
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

            val labels = ArrayList<String>()
            for(i in 0 until 32){
                labels.add(i.toString())
            }

            _monthBarData.value = BarData(labels, barDataSet)

        }
    }

    fun setLastSevenStudySessionsData(currentMonth: Int, currentDayOfMonth: Int){
        viewModelScope.launch {
            _lastSevenStudySession.value = repository.getLastSevenSessions(currentMonth, currentDayOfMonth)

            val entries = ArrayList<BarEntry>()

            for(session in _lastSevenStudySession.value!!.indices){
                entries.add(BarEntry(_lastSevenStudySession.value!![session].hours, session))
            }

            val barDataSet = BarDataSet(entries, "Cells")

            val labels = ArrayList<String>()
            for(i in 0 until 32){
                labels.add(i.toString())
            }

            _weekBarData.value = BarData(labels, barDataSet)
        }
    }



    //Inserts mock study session from MainActivity - already inserted into database though
    fun insertStudySession(){
        viewModelScope.launch {

            for (session in 0 until 31) {

                var study = Study(
                    hours = 4F,
                    minutes = 60,
                    date = "2020-08-27",
                    weekDay = "WEDNESDAY",
                    month = 8,
                    dayOfMonth = session

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