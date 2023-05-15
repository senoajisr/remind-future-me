package org.d3if3109.mobpro1.ui.showreminder

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import org.d3if3109.mobpro1.R
import org.d3if3109.mobpro1.adapter.ReminderAdapter
import org.d3if3109.mobpro1.databinding.FragmentShowReminderBinding
import org.d3if3109.mobpro1.db.ReminderDb
import org.d3if3109.mobpro1.db.ReminderEntity
import java.util.Calendar


class ShowReminderFragment : Fragment() {
    private lateinit var binding: FragmentShowReminderBinding
    private lateinit var reminderAdapter: ReminderAdapter

    private val viewModel: ShowReminderViewModel by lazy {
        val reminderDb = ReminderDb.getInstance(requireContext())
        val factory = ShowReminderViewModelFactory(reminderDb.dao)
        ViewModelProvider(this, factory)[ShowReminderViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentShowReminderBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reminderAdapter = ReminderAdapter()

        with(binding.reminderRecyclerView) {
            adapter = reminderAdapter
            // setHasFixedSize(true)
        }

        viewModel.reminderData.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            reminderAdapter.submitList(it)
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
        context?.let {
            DatePickerDialog(
                it,
                { _, year, month, dayOfMonth -> binding.dueDateButton.text = resources.getString(
                            R.string.date_format,
                            dayOfMonth.toString(),
                            month + 1,
                            year
                        )
                },
                mYear,
                mMonth,
                mDay
            )
        }?.show()
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
                val position = viewHolder.adapterPosition
                val deletedReminder: ReminderEntity? = viewModel.reminderData.value?.get(position)

                if (deletedReminder != null) {
                    viewModel.removeReminderAt(deletedReminder.id)

                    Snackbar.make(
                        binding.reminderRecyclerView,
                        "Deleted " + deletedReminder.title,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(
                            "Undo"
                        ) {
                            viewModel.insertReminderEntity(deletedReminder)
                        }.show()
                }
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

        viewModel.insertReminder(title, description, dueDate)
    }

}