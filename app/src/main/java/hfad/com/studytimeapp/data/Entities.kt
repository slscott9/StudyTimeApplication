package hfad.com.studytimeapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_table")
data class Study(
    @PrimaryKey
    val date : String,
    val hours: Float,
    val minutes: Long,
    val weekDay: String,
    val dayOfMonth: Int,
    val month: Int
) {
}