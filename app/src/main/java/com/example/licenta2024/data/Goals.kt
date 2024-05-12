package com.example.licenta2024.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Goals")
data class Goals(
    @PrimaryKey(autoGenerate = true) val goalId: Long = 0,
    var calories: Int,
    var protein: Int,
    var carbs: Int,
    var fats: Int,
    var stepGoal: Int,
    var waterIntakeGoal: Int,
    var bmr: Int,
    var tdee: Int
)