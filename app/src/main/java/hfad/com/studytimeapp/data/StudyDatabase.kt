package hfad.com.studytimeapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Study::class], version = 5, exportSchema = false)
abstract class StudyDatabase : RoomDatabase() {

    abstract fun studyDao(): StudyDao

    companion object {
        @Volatile
        private var INSTANCE: StudyDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): StudyDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE
                ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        StudyDatabase::class.java,
                        "study_database"
                    )
                        // Wipes and rebuilds instead of migrating if no Migration object.
                        // Migration is not part of this codelab.
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                    // return instance
                    instance
                }
        }
    }
}