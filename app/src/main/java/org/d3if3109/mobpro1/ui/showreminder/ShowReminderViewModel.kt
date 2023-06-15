package org.d3if3109.mobpro1.ui.showreminder

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if3109.mobpro1.db.ReminderDao
import org.d3if3109.mobpro1.db.ReminderEntity
import org.d3if3109.mobpro1.network.ReminderApi

class ShowReminderViewModel(private val reminderDao: ReminderDao) : ViewModel() {
    val reminderData = reminderDao.getAllReminder()

    init {
        retrieveData()
    }

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

    private fun retrieveData() {
        viewModelScope.launch (Dispatchers.IO) {
            try {
                val result = ReminderApi.service.getReminder()
                Log.d("MainViewModel", "Success: $result")
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
            }
        }
    }
}