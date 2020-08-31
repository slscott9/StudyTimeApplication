package hfad.com.studytimeapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class StudyRepository(private val studyDao: StudyDao) {



    suspend fun getLastSevenSessions(currentMonth: Int, currentDayOfMonth: Int): List<Study>{
        Log.i("StudyRepo", "current month is $currentMonth currentDayOfMonth is $currentDayOfMonth offset is ")
        return studyDao.getLastSevenSessions(currentMonth, currentDayOfMonth )
    }


    suspend fun insertStudySession(study: Study){
        studyDao.upsertStudySession(study)
    }
    suspend fun getCurrentStudySession(currentDate: String): Study{
        return studyDao.getCurrentStudySession(currentDate)
    }

    suspend fun getAllSessionsWithMatchingMonth(monthSelected: Int): MutableList<Study>{
        return studyDao.getAllSessionsWithMatchingMonth(monthSelected)
    }

    suspend fun getYearsWithSessions(currentYear: Int): List<Int>{
        return studyDao.getYearsWithSessions(currentYear)
    }

//    suspend fun updateStudySession(currentMonth: Int, currentDayOfMonth: Int, newHours: Float){
//        studyDao.updateStudySession(currentMonth, currentDayOfMonth, newHours)
//    }
}