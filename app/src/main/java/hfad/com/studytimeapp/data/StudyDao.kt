package hfad.com.studytimeapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface StudyDao {

    @Query("select * from study_table where month= :currentMonth and dayOfMonth between :currentDayOfMonth - 7 and :currentDayOfMonth")
    suspend fun getLastSevenSessions(currentMonth: Int, currentDayOfMonth: Int) : List<Study>

    @Insert
    suspend fun insertStudySession(study: Study)

    @Query("select * from study_table where date= :currentDate ")
    suspend fun getCurrentStudySession(currentDate: String): Study


    @Query("select * from study_table where month= :monthSelected")
    suspend fun getAllSessionsWithMatchingMonth(monthSelected: Int): List<Study>
}