package hfad.com.studytimeapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface StudyDao {


    @Query("select * from study_table where date= :currentDate ")
    suspend fun getCurrentStudySession(currentDate: String): Study


    @Query("select * from study_table where month= :monthSelected")
    suspend fun getAllSessionsWithMatchingMonth(monthSelected: Int): List<Study>

    @Query("select * from study_table where month= :currentMonth and dayOfMonth between :currentDayOfMonth - 6 and :currentDayOfMonth order by dayOfMonth asc")
    suspend fun getLastSevenSessions(currentMonth: Int, currentDayOfMonth: Int): List<Study>




    /*
        The problem is when the database is empty how do you know when to call updateStudySession or insert a study session
     */

    @Transaction
    suspend fun upsertStudySession(study: Study ){
        val inserted = insertStudySession(study)

        if(inserted == -1L){
            updateStudySession(study.month, study.dayOfMonth, study.hours)
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStudySession(study: Study): Long

    @Query("update study_table set hours = hours + :a  where dayOfMonth= :currentDayOfMonth and month= :currentMonth")
    suspend fun updateStudySession(currentMonth: Int, currentDayOfMonth: Int, a: Float )

}