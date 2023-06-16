package org.d3if3109.mobpro1.ui.showreminder

import android.util.Log
import androidx.lifecycle.MutableLiveData
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
    val status = MutableLiveData<ApiStatus>()

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

    private fun insertReminder(title: String, description: String, dueDate: String) {
        val entity = ReminderEntity(
            title = title,
            description = description,
            dueDate = dueDate,
        )

        insertReminderEntity(entity)
    }

    private fun retrieveData() {
        status.postValue(ApiStatus.LOADING)
        viewModelScope.launch (Dispatchers.IO) {
            try {
                val reminder = ReminderApi.service.getReminder()
                insertReminder(reminder.title, reminder.description, reminder.dueDate)
                status.postValue(ApiStatus.SUCCESS)
            } catch (e: Exception) {
                Log.d("ShowReminderViewModel", "Failure: ${e.message}")
                status.postValue(ApiStatus.FAILED)
            }
        }
    }

    enum class ApiStatus { LOADING, SUCCESS, FAILED }
}