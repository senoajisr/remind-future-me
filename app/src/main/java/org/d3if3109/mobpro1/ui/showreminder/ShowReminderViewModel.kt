package org.d3if3109.mobpro1.ui.showreminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if3109.mobpro1.db.ReminderDao
import org.d3if3109.mobpro1.db.ReminderEntity

class ShowReminderViewModel(private val reminderDao: ReminderDao) : ViewModel() {
    val reminderData = reminderDao.getAllReminder()

    fun removeReminderAt(id: Long) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            reminderDao.deleteById(id)
        }
    }

    fun insertReminderEntity(entity: ReminderEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                reminderDao.insert(entity)
            }
        }
    }
}