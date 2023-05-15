package org.d3if3109.mobpro1.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ReminderEntity::class], version = 1, exportSchema = false)
abstract class ReminderDb : RoomDatabase() {
    abstract val dao: ReminderDao

    companion object {
        @Volatile
        private var INSTANCE: ReminderDb? = null

        fun getInstance(context: Context): ReminderDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ReminderDb::class.java,
                        "reminder.db",
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}