package org.d3if3109.mobpro1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.d3if3109.mobpro1.databinding.ReminderItemBinding
import org.d3if3109.mobpro1.db.ReminderEntity
import org.d3if3109.mobpro1.ui.showreminder.ShowReminderFragmentDirections


class ReminderAdapter : ListAdapter<ReminderEntity, ReminderAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ReminderItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ReminderItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(reminder: ReminderEntity) = with(binding) {
            titleTextView.text = reminder.title

            if (reminder.description == "") {
                descriptionTextView.visibility = View.GONE
            }

            if (reminder.dueDate == "") {
                dueDateTextView.visibility = View.GONE
            }

            descriptionTextView.text = reminder.description
            dueDateTextView.text = reminder.dueDate

            root.setOnClickListener {
                val action = ShowReminderFragmentDirections.actionShowReminderFragmentToViewReminderFragment(reminder.id)
                it.findNavController().navigate(action)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<ReminderEntity>() {
                override fun areItemsTheSame(
                    oldData: ReminderEntity, newData: ReminderEntity
                ): Boolean {
                    return oldData.id == newData.id
                }

                override fun areContentsTheSame(
                    oldData: ReminderEntity, newData: ReminderEntity
                ): Boolean {
                    return oldData == newData
            }
        }
    }
}