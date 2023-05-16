package org.d3if3109.mobpro1.ui.addreminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3if3109.mobpro1.db.ReminderDao

class AddReminderViewModelFactory(private val reminderDao: ReminderDao) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddReminderViewModel::class.java)) {
            return AddReminderViewModel(reminderDao) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}