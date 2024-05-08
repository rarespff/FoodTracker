package com.example.licenta2024.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Day")
data class Day(
    @PrimaryKey(autoGenerate = false) val dateId: String,
    val dayNumber: String,
    val dayName: String,
    val dayMonth: String,
    var totalConsumedCalories: Double,
    var totalBurnedCalories: Double,
    var proteinIntake: Double,
    var carbsIntake: Double,
    var fatsIntake: Double,
    var waterIntake: Int,
    var breakfast: List<Food>,
    var lunch: List<Food>,
    var dinner: List<Food>,
    var steps: Int
)