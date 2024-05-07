package com.example.licenta2024.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DayListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromDayList(dayList: List<Day>): String {
        return gson.toJson(dayList)
    }

    @TypeConverter
    fun toDayList(dayListString: String): List<Day> {
        val listType = object : TypeToken<List<Day>>() {}.type
        return gson.fromJson(dayListString, listType)
    }
}
