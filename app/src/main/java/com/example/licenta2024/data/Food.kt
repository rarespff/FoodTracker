package com.example.licenta2024.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Food")
data class Food(
    @PrimaryKey(autoGenerate = true) val foodId: Long = 0,
    val name: String = "",
    val calories: Int = 0,
    val protein: Int = 0,
    val carbs: Int = 0,
    val fats: Int = 0,
    val quantity: Int = 0
) {
    // Default constructor
    constructor() : this(
        foodId = 0,
        name = "",
        calories = 0,
        protein = 0,
        carbs = 0,
        fats = 0,
        quantity = 0
    )
}