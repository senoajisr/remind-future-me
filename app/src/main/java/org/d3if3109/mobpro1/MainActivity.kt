package org.d3if3109.mobpro1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.d3if3109.mobpro1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var reminders: MutableList<Reminder> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding.reminderRecyclerView) {
            adapter = ReminderAdapter(reminders)
        }

        binding.addButton.setOnClickListener { onAddButtonClicked() }
    }

    private fun onAddButtonClicked() {
        val title: String = binding.titleTextInput.text.toString()
        val description: String = binding.descriptionTextInput.text.toString()

        if (title == "") {
            binding.titleTextInputLayout.error = "Title cannot be empty"
            return
        }

        binding.titleTextInputLayout.error = ""
        reminders.add(0, Reminder(title, description))

        with(binding.reminderRecyclerView) {
            adapter!!.notifyItemInserted(0)
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