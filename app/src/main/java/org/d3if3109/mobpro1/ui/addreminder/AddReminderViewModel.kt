package org.d3if3109.mobpro1.ui.addreminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if3109.mobpro1.db.ReminderDao
import org.d3if3109.mobpro1.db.ReminderEntity

class AddReminderViewModel(private val reminderDao: ReminderDao) : ViewModel() {

    fun insertReminder(title: String, description: String, dueDate: String) {
        val entity = ReminderEntity(
            title = title,
            description = description,
            dueDate = dueDate,
        )

        insertReminderEntity(entity)
    }

    private fun insertReminderEntity(entity: ReminderEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                reminderDao.insert(entity)
            }
        }
    }
}