package com.example.licenta2024.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Goals")
data class Goals(
    @PrimaryKey(autoGenerate = true) val goalId: Long = 0,
    var calories: Int = 0,
    var protein: Int = 0,
    var carbs: Int = 0,
    var fats: Int = 0,
    var stepGoal: Int = 0,
    var waterIntakeGoal: Int = 0,
    var bmr: Int = 0,
    var tdee: Int = 0
) {
    // Default constructor
    constructor() : this(
        goalId = 0,
        calories = 0,
        protein = 0,
        carbs = 0,
        fats = 0,
        stepGoal = 0,
        waterIntakeGoal = 0,
        bmr = 0,
        tdee = 0
    )
}
