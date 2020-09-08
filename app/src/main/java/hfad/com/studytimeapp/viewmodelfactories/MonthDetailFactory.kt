package hfad.com.studytimeapp.viewmodelfactories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hfad.com.studytimeapp.data.StudyDao
import hfad.com.studytimeapp.viewmodels.MonthDetailViewModel
import hfad.com.studytimeapp.viewmodels.SessionMonthSelectorViewModel

class MonthDetailFactory(
    private val monthSelected: Int,
    private val application: Application,
    private val studyDao: StudyDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MonthDetailViewModel::class.java)) {
            return MonthDetailViewModel(monthSelected, application, studyDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}