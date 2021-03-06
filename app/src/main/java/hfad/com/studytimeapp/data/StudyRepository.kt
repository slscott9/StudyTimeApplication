package hfad.com.studytimeapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/*
    If methods return live data Room takes care of coroutines and Dispatchers for us

    If the method does not return live data need to launch from coroutine scope and use dispatchers
 */

class StudyRepository(private val studyDao: StudyDao) {

    //New method to get last seven sessions hours
      fun getLastSevenSessionsHours(currentMonth: Int, currentDayOfMonth: Int): LiveData<List<Float>>{
        return studyDao.getLastSevenSessionsHours(currentMonth, currentDayOfMonth)
    }

    fun getSessionHoursForMonth(currentMonth: Int): LiveData<List<Float>>{
        return studyDao.getSessionHoursForMonth(currentMonth)
    }



     fun getLastSevenSessions(currentMonth: Int, currentDayOfMonth: Int): LiveData<List<Study>>{
        Log.i("StudyRepo", "current month is $currentMonth currentDayOfMonth is $currentDayOfMonth offset is ")
        return studyDao.getLastSevenSessions(currentMonth, currentDayOfMonth )
    }

     fun getAllSessionsWithMatchingMonth(monthSelected: Int): LiveData<List<Study>>{ //returns live data so it does not need suspend. Room takes care of coroutines for us
        return studyDao.getAllSessionsWithMatchingMonth(monthSelected)
    }


    suspend fun insertStudySession(study: Study){
        studyDao.upsertStudySession(study)
    }
    suspend fun getCurrentStudySession(currentDate: String): Study{
        return studyDao.getCurrentStudySession(currentDate)
    }



    suspend fun getYearsWithSessions(currentYear: Int): List<Int>{
        return studyDao.getYearsWithSessions(currentYear)
    }

    suspend fun getMonthWithSelectedYear(yearSelected : Int): List<Int>{
       return studyDao.getMonthsWithSelectedYear(yearSelected)
    }
//    suspend fun updateStudySession(currentMonth: Int, currentDayOfMonth: Int, newHours: Float){
//        studyDao.updateStudySession(currentMonth, currentDayOfMonth, newHours)
//    }
}