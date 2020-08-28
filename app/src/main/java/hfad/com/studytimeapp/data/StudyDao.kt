package hfad.com.studytimeapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface StudyDao {

//    @Query("select dayOfMonth, sum(hours) from study_table where month= :currentMonth and dayOfMonth between :currentDayOfMonth - 7 and :currentDayOfMonth group by dayOfMonth ")
//    suspend fun getLastSevenSessions(currentMonth: Int, currentDayOfMonth: Int) : List<Study>



    @Query("select * from study_table where date= :currentDate ")
    suspend fun getCurrentStudySession(currentDate: String): Study


    @Query("select * from study_table where month= :monthSelected")
    suspend fun getAllSessionsWithMatchingMonth(monthSelected: Int): List<Study>

    @Insert
    suspend fun insertStudySession(study: Study)


//This query updates a currently existing study session
    @Query("update study_table set hours = hours + :a  where dayOfMonth= :currentDayOfMonth and month= :currentMonth")
    suspend fun updateStudySession(currentMonth: Int, currentDayOfMonth: Int, a: Float )

}