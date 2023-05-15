package org.d3if3109.mobpro1.ui.showreminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3if3109.mobpro1.db.ReminderDao

class ShowReminderViewModelFactory(private val reminderDao: ReminderDao) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowReminderViewModel::class.java)) {
            return ShowReminderViewModel(reminderDao) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}