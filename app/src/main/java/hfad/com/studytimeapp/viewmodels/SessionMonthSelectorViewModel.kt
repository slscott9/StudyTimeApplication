package hfad.com.studytimeapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hfad.com.studytimeapp.data.StudyDao
import hfad.com.studytimeapp.data.StudyDatabase
import hfad.com.studytimeapp.data.StudyRepository
import kotlinx.coroutines.*

class SessionMonthSelectorViewModel(currentYear: Int, application: Application, studyDao: StudyDao) : AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    private val repository = StudyRepository(studyDao)

    private val _yearList = MutableLiveData<List<Int>>()
    var yearsList: LiveData<List<Int>> = _yearList

    private val _monthsFromSelectedYear = MutableLiveData<List<Int>>()
    var monthsFromSelectedYear: LiveData<List<Int>> = _monthsFromSelectedYear



    init {
        initYearsList(currentYear)
    }

    private fun initYearsList(currentYear: Int){
        viewModelScope.launch {
            _yearList.value = repository.getYearsWithSessions(currentYear)
        }
    }

    fun getMonthsWithSelectedYear(selectedYear: Int){
        viewModelScope.launch {
            _monthsFromSelectedYear.value = repository.getMonthWithSelectedYear(selectedYear)
        }
    }












}