package org.d3if3109.mobpro1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.d3if3109.mobpro1.databinding.ReminderItemBinding


class ReminderAdapter(items: MutableList<Reminder>) : RecyclerView.Adapter<ReminderAdapter.ViewHolder>() {
    var recycler_items: MutableList<Reminder> = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ReminderItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    fun updateData(new_items: MutableList<Reminder>) {
        recycler_items.clear()
        recycler_items.addAll(new_items)
        notifyDataSetChanged()
    }

    fun addItem(position: Int, reminder: Reminder) {
        recycler_items.add(position, reminder)
        notifyItemInserted(position)
    }

    fun removeItem(position: Int) {
        recycler_items.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recycler_items[position])
    }

    override fun getItemCount(): Int {
        return recycler_items.size
    }

    class ViewHolder(private val binding: ReminderItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(reminder: Reminder) = with(binding) {
            titleTextView.text = reminder.title
            descriptionTextView.text = reminder.description
        }
    }

}