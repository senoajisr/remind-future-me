package org.d3if3109.mobpro1.ui.addreminder

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.d3if3109.mobpro1.R
import org.d3if3109.mobpro1.databinding.FragmentAddReminderBinding
import org.d3if3109.mobpro1.db.ReminderDb
import java.util.Calendar

class AddReminderFragment : Fragment() {
    private lateinit var binding: FragmentAddReminderBinding

    private val viewModel: AddReminderViewModel by lazy {
        val reminderDb = ReminderDb.getInstance(requireContext())
        val factory = AddReminderViewModelFactory(reminderDb.dao)
        ViewModelProvider(this, factory)[AddReminderViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAddReminderBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addButton.setOnClickListener { onAddButtonClicked() }
        binding.dueDateButton.setOnClickListener { onDateTextInputClicked() }
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