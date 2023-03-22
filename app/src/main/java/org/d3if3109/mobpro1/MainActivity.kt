package org.d3if3109.mobpro1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import org.d3if3109.mobpro1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding.reminderRecyclerView) {
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = ReminderAdapter(getDummyReminderData())
            setHasFixedSize(true)
        }
    }

    private fun getDummyReminderData() : MutableList<Reminder> {
        return mutableListOf(
            Reminder("Homework", "English homework at page 17"),
            Reminder("Buy mobile data", ""),
            Reminder("Project progress meeting", "Topics:\n- Major bugs\n- Minor Bugs"),
        )
    }

}