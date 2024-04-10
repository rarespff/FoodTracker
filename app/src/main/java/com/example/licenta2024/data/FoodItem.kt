package com.example.licenta2024.data

// Define a data class to represent a food item
data class FoodItem(
    val description: String,
    val fdcId: String,
    val calories: Int,
    val protein: Double,
    val fat: Double,
    val carbohydrates: Double,
    val brandName: String,
    val packageWeight: String
)