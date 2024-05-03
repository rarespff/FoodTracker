package com.example.licenta2024.page.main.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.licenta2024.R
import com.example.licenta2024.page.main.MainViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var calendarRv: RecyclerView
    private lateinit var adapter: JournalDayAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.home_fragment, container, false)
        calendarRv = rootView.findViewById(R.id.calendar_rv)
        val customToolbar = rootView.findViewById<LinearLayout>(R.id.customToolbar)
        val dayNameTextView = customToolbar.findViewById<TextView>(R.id.dayNameTextView)
        val days = generateDayItems()
        adapter = JournalDayAdapter(calendarRv, days, dayNameTextView) { day, position ->
            if (position >= 3) {
                calendarRv.scrollToPosition(position - 3)
            } else {
                calendarRv.scrollToPosition(0)
            }
            dayNameTextView.text = getFullDayName(day.dayName)
        }
        calendarRv.adapter = adapter
        return rootView
    }

    private fun generateDayItems(): List<DayItem> {
        val calendar = Calendar.getInstance()
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val dayItems = mutableListOf<DayItem>()
        val dateFormat = SimpleDateFormat("E", Locale.getDefault())

        for (i in 1..daysInMonth) {
            calendar.set(Calendar.DAY_OF_MONTH, i)
            val dayName = dateFormat.format(calendar.time)
            dayItems.add(DayItem(i.toString(), dayName))
        }
        return dayItems
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
            else -> throw IllegalArgumentException("Invalid day name: $shortDayName")
        }
    }
}