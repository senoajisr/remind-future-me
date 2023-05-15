package org.d3if3109.mobpro1.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var title: String,
    var description: String,
    var dueDate: String,
)
