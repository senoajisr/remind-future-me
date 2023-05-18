package org.d3if3109.mobpro1.ui.viewreminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3if3109.mobpro1.db.ReminderDao


class ViewReminderViewModelFactory(private val reminderDao: ReminderDao) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewReminderViewModel::class.java)) {
            return ViewReminderViewModel(reminderDao) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}