package org.d3if3109.mobpro1.ui.viewreminder

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.d3if3109.mobpro1.R
import org.d3if3109.mobpro1.databinding.FragmentViewReminderBinding
import org.d3if3109.mobpro1.db.ReminderDb
import org.d3if3109.mobpro1.db.ReminderEntity

class ViewReminderFragment : Fragment() {

    private lateinit var binding: FragmentViewReminderBinding
    private lateinit var reminderData: LiveData<ReminderEntity>

    private val args: ViewReminderFragmentArgs by navArgs()

    private val viewModel: ViewReminderViewModel by lazy {
        val reminderDb = ReminderDb.getInstance(requireContext())
        val factory = ViewReminderViewModelFactory(reminderDb.dao)
        ViewModelProvider(this, factory)[ViewReminderViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentViewReminderBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reminderData = viewModel.getReminderById(args.reminderId)

        with(binding) {
            reminderData.observe(viewLifecycleOwner) {
                titleViewReminderTextView.text = it.title
                dueDateViewReminderTextView.text = it.dueDate
                descriptionViewReminderTextView.text = it.description
            }

            deleteViewReminderButton.setOnClickListener { onDeleteButtonClicked(it) }
            shareViewReminderButton.setOnClickListener { onShareButtonClicked() }
        }
    }

    private fun onDeleteButtonClicked(view: View) {
        reminderData.observe(viewLifecycleOwner) {
            MaterialAlertDialogBuilder(requireContext())
                .setMessage(R.string.confirm_deletion)
                .setPositiveButton(getString(R.string.delete)) { _, _ ->
                    viewModel.removeReminderById(it.id)
                    view.findNavController().navigateUp()
                }
                .setNegativeButton(getString(R.string.cancled)) { dialog, _ ->
                    dialog.cancel()
                }
                .show()
        }
    }

    private fun onShareButtonClicked() {
        val title = binding.titleViewReminderTextView.text
        val dueDate = binding.dueDateViewReminderTextView.text
        val description = binding.descriptionViewReminderTextView.text

        val message = getString(
            R.string.share_template,
            title,
            dueDate,
            description,
        )
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain").putExtra(Intent.EXTRA_TEXT, message)
        if (shareIntent.resolveActivity(
                requireActivity().packageManager) != null) {
            startActivity(shareIntent)
        }
    }
}