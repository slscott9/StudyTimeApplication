package hfad.com.studytimeapp.data

import android.util.Log
import androidx.lifecycle.LiveData

class StudyRepository(private val studyDao: StudyDao) {

    suspend fun getLastSevenSessions(currentMonth: Int, currentDayOfMonth: Int): List<Float>{
        Log.i("StudyRepo", "current month is $currentMonth currentDayOfMonth is $currentDayOfMonth offset is ")
        return studyDao.getLastSevenSessions(currentMonth, currentDayOfMonth )
    }


    suspend fun insertStudySession(study: Study){
        studyDao.insertStudySession(study)
    }
    suspend fun getCurrentStudySession(currentDate: String): Study{
        return studyDao.getCurrentStudySession(currentDate)
    }

    suspend fun getAllSessionsWithMatchingMonth(monthSelected: Int): List<Study>{
        return studyDao.getAllSessionsWithMatchingMonth(monthSelected)
    }
}