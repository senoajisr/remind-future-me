package org.d3if3109.mobpro1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
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

        addReminderItemSwipeRightAction()
    }


    private fun addReminderItemSwipeRightAction() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // From https://www.geeksforgeeks.org/android-swipe-to-delete-and-undo-in-recyclerview-with-kotlin/
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.reminderRecyclerView.adapter
                val position = viewHolder.adapterPosition
                val deletedReminder: Reminder = reminders[position]

                reminders.removeAt(position)
                adapter!!.notifyItemRemoved(position)

                Snackbar.make(
                    binding.reminderRecyclerView,
                    "Deleted " + deletedReminder.title,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(
                        "Undo",
                        View.OnClickListener {
                            reminders.add(position, deletedReminder)
                            adapter!!.notifyItemInserted(position)
                        }).show()
            }
        }).attachToRecyclerView(binding.reminderRecyclerView)
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