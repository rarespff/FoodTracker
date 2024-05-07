package com.example.licenta2024.data

import androidx.room.TypeConverter
import com.google.gson.Gson

class GoalsConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromGoals(goals: Goals?): String? {
        return gson.toJson(goals)
    }

    @TypeConverter
    fun toGoals(goalsJson: String?): Goals? {
        return gson.fromJson(goalsJson, Goals::class.java)
    }
}
