package org.d3if3109.mobpro1.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ReminderDao {
    @Insert
    fun insert(reminder: ReminderEntity)

    @Query("SELECT * FROM reminder ORDER BY id DESC")
    fun getAllReminder(): LiveData<List<ReminderEntity>>

    @Query("DELETE FROM reminder WHERE id = :id")
    fun deleteById(id: Long)
}