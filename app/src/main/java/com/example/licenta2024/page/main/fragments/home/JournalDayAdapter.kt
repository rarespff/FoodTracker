package com.example.licenta2024.page.main.fragments.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.licenta2024.R
import java.util.Calendar
import java.util.Locale

class JournalDayAdapter(
    recyclerView: RecyclerView,
    private val days: List<DayItem>,
    private val dayName: TextView,
    private val onItemClick: (DayItem, Int) -> Unit
) :
    RecyclerView.Adapter<JournalDayAdapter.CircleViewHolder>() {
    private var selectedPosition = -1

    init {
        val calendar = Calendar.getInstance()
        val today = calendar.get(Calendar.DAY_OF_MONTH).toString()
        for ((index, dayItem) in days.withIndex()) {
            if (dayItem.day == today) {
                selectedPosition = index
                dayName.text = getFullDayName(dayItem.dayName)
                break
            }
        }
        if (selectedPosition >= 3) {
            recyclerView.scrollToPosition(selectedPosition - 3)
        } else {
            recyclerView.scrollToPosition(0)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CircleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.day_item, parent, false)
        return CircleViewHolder(view)
    }

    override fun onBindViewHolder(holder: CircleViewHolder, position: Int) {
        val dayItem = days[position]
        holder.bind(dayItem, position == selectedPosition)
        holder.itemView.setOnClickListener {
            selectedPosition = holder.adapterPosition
            notifyDataSetChanged()
            onItemClick(dayItem, position)
        }
    }

    override fun getItemCount(): Int {
        return days.size
    }

    class CircleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dayNameTextView: TextView = itemView.findViewById(R.id.dayNameTextView)
        private val dayNumberTextView: TextView = itemView.findViewById(R.id.dayNumberTextView)

        fun bind(dayItem: DayItem, isSelected: Boolean) {
            dayNameTextView.text = dayItem.dayName
            dayNumberTextView.text = dayItem.day
            itemView.isSelected = isSelected
        }
    }

    private fun getFullDayName(shortDayName: String): String {
        return when (shortDayName.lowercase(Locale.ROOT)) {
            "sun" -> "Sunday"
            "mon" -> "Monday"
            "tue" -> "Tuesday"
            "wed" -> "Wednesday"
            "thu" -> "Thursday"
            "fri" -> "Friday"
            "sat" -> "Saturday"
            "dum." -> "Sunday"
            "lun." -> "Monday"
            "mar." -> "Tuesday"
            "mie." -> "Wednesday"
            "joi." -> "Thursday"
            "vin." -> "Friday"
            "sam." -> "Saturday"
            else -> throw IllegalArgumentException("Invalid day name: $shortDayName")
        }
    }

}
