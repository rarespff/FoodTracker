package com.example.licenta2024.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Day")
data class Day(
    @PrimaryKey(autoGenerate = false) val dateId: String = "",
    val dayNumber: String = "",
    val dayName: String = "",
    val dayMonth: String = "",
    var totalConsumedCalories: Int = 0,
    var totalBurnedCalories: Int = 0,
    var proteinIntake: Int = 0,
    var carbsIntake: Int = 0,
    var fatsIntake: Int = 0,
    var waterIntake: Int = 0,
    var breakfast: List<Food> = emptyList(),
    var lunch: List<Food> = emptyList(),
    var dinner: List<Food> = emptyList(),
    var steps: Int = 0
) {
    // Default constructor
    constructor() : this(
        dateId = "",
        dayNumber = "",
        dayName = "",
        dayMonth = "",
        totalConsumedCalories = 0,
        totalBurnedCalories = 0,
        proteinIntake = 0,
        carbsIntake = 0,
        fatsIntake = 0,
        waterIntake = 0,
        breakfast = emptyList(),
        lunch = emptyList(),
        dinner = emptyList(),
        steps = 0
    )
}