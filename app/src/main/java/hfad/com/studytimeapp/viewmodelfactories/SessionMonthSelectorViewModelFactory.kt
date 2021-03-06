package hfad.com.studytimeapp.viewmodelfactories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hfad.com.studytimeapp.data.StudyDao
import hfad.com.studytimeapp.viewmodels.SessionMonthSelectorViewModel

class SessionMonthSelectorViewModelFactory(
    private val currentYear: Int,
    private val application: Application,
    private val studyDao: StudyDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SessionMonthSelectorViewModel::class.java)) {
            return SessionMonthSelectorViewModel(currentYear, application, studyDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}