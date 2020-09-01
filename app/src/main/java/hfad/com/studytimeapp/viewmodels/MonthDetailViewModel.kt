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

class MonthDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val studyDao = StudyDatabase.getDatabase(application, viewModelScope).studyDao()
    private val repository = StudyRepository(studyDao)

    private val monthDayLabels = arrayListOf<String>("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31")
    private val months = arrayListOf<String>("January", "February" ,"March", "April", "May", "June", "July", "August", "September", "October", "November", "December")



    private val _monthBarData = MutableLiveData<BarData>()
    val monthBarData: LiveData<BarData>
        get() = _monthBarData

    var month: String = ""

    private val allSessionWithMatchingMonth = MutableLiveData<MutableList<Study>>()



    fun setAllSelectedMonthData(seletedMonth: Int){
        viewModelScope.launch {
            allSessionWithMatchingMonth.value = repository.getAllSessionsWithMatchingMonth(seletedMonth)

            val monthBarDataSetValues = MutableList(31){ BarEntry(0F, 0) }

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
}