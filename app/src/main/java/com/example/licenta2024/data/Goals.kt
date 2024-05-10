package com.example.licenta2024.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Goals")
data class Goals(
    @PrimaryKey(autoGenerate = true) val goalId: Long = 0,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fats: Int,
    val stepGoal: Int,
    val waterIntakeGoal: Int,
    val bmr: Int,
    val tdee: Int
)