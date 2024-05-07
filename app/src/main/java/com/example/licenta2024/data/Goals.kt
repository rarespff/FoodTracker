package com.example.licenta2024.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Goals")
data class Goals(
    @PrimaryKey(autoGenerate = true) val goalId: Long = 0,
    val calories: Double,
    val protein: Double,
    val carbs: Double,
    val fats: Double,
    val stepGoal: Int,
    val waterIntakeGoal: Int
)