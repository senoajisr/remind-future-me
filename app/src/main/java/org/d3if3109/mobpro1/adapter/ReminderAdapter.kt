package org.d3if3109.mobpro1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.d3if3109.mobpro1.databinding.ReminderItemBinding
import org.d3if3109.mobpro1.model.Reminder


class ReminderAdapter(items: MutableList<Reminder>) : RecyclerView.Adapter<ReminderAdapter.ViewHolder>() {
    var recyclerItems: MutableList<Reminder> = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ReminderItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    fun updateData(new_items: MutableList<Reminder>) {
        recyclerItems.clear()
        recyclerItems.addAll(new_items)
        notifyDataSetChanged()
    }

    fun addItem(reminder: Reminder, position: Int) {
        recyclerItems.add(position, reminder)
        notifyItemInserted(position)
    }

    fun removeItem(position: Int) {
        recyclerItems.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recyclerItems[position])
    }

    override fun getItemCount(): Int {
        return recyclerItems.size
    }

    class ViewHolder(private val binding: ReminderItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(reminder: Reminder) = with(binding) {
            titleTextView.text = reminder.title

            if (reminder.description == "") {
                descriptionTextView.visibility = View.GONE
            }

            if (reminder.dueDate == "") {
                dueDateTextView.visibility = View.GONE
            }

            descriptionTextView.text = reminder.description
            dueDateTextView.text = reminder.dueDate
        }
    }

}