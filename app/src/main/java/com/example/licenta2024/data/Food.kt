package com.example.licenta2024.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Food")
data class Food(
    @PrimaryKey(autoGenerate = true) val foodId: Long = 0,
    val name: String,
    val calories: Double,
    val protein: Double,
    val carbs: Double,
    val fats: Double,
    val quantity: Double
)