package org.d3if3109.mobpro1

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import org.d3if3109.mobpro1.databinding.ActivityMainBinding
import java.util.Calendar


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
        binding.dueDateButton.setOnClickListener { onDateTextInputClicked() }

        addReminderItemSwipeRightAction()
    }


    // From https://www.techypid.com/datepicker-dialog-click-on-edittext-in-android/
    private fun onDateTextInputClicked() {
        val calendar: Calendar = Calendar.getInstance()
        val mYear: Int = calendar.get(Calendar.YEAR)
        val mMonth: Int = calendar.get(Calendar.MONTH)
        val mDay: Int = calendar.get(Calendar.DAY_OF_MONTH)
        //show dialog
        val datePickerDialog = DatePickerDialog(this,
            { _, year, month, dayOfMonth -> binding.dueDateButton.text =
                resources.getString(R.string.date_format, dayOfMonth.toString(), month + 1, year) },
            mYear,
            mMonth,
            mDay
        )
        datePickerDialog.show()
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
                        "Undo"
                    ) {
                        reminders.add(position, deletedReminder)
                        adapter.notifyItemInserted(position)
                    }.show()
            }
        }).attachToRecyclerView(binding.reminderRecyclerView)
    }

    private fun onAddButtonClicked() {
        val title: String = binding.titleTextInput.text.toString()
        val description: String = binding.descriptionTextInput.text.toString()
        var dueDate: String = binding.dueDateButton.text.toString()

        if (title == "") {
            binding.titleTextInputLayout.error = "Title cannot be empty"
            return
        }

        if (dueDate == "Due Date") {
            dueDate = ""
        }

        binding.titleTextInputLayout.error = ""
        reminders.add(0, Reminder(title, description, dueDate))

        with(binding.reminderRecyclerView) {
            adapter!!.notifyItemInserted(0)
        }
    }

}