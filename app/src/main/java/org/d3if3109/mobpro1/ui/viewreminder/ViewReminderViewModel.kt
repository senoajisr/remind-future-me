package org.d3if3109.mobpro1.ui.viewreminder

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if3109.mobpro1.db.ReminderDao
import org.d3if3109.mobpro1.db.ReminderEntity

class ViewReminderViewModel(private val reminderDao: ReminderDao) : ViewModel() {

    fun getReminderById(id: Long): LiveData<ReminderEntity> {
        return reminderDao.getReminderById(id)
    }

    fun removeReminderById(id: Long) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            reminderDao.deleteById(id)
        }
    }
}