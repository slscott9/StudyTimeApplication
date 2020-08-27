package hfad.com.studytimeapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_table")
data class Study(
    @PrimaryKey(autoGenerate = true)
    val row_id: Int = 0,
    val hours: Float,
    val minutes: Long,
    val date : String,
    val weekDay: String,
    val dayOfMonth: Int,
    val month: Int
) {
}