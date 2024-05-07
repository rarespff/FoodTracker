package com.example.licenta2024.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Day")
data class Day(
    @PrimaryKey(autoGenerate = false) val dateId: String,
    val dayNumber: String,
    val dayName: String,
    val dayMonth: String,
    val totalConsumedCalories: Double,
    val totalBurnedCalories: Double,
    val proteinIntake: Double,
    val carbsIntake: Double,
    val fatsIntake: Double,
    val waterIntake: Int,
    var breakfast: List<Food>,
    var lunch: List<Food>,
    var dinner: List<Food>,
    val steps: Int
)