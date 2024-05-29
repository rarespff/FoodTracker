package com.example.licenta2024.data

import com.squareup.moshi.Json

data class SearchedFoodResult(
    val id: Int?,
    val title: String?,
    val calories: Int?,
    val protein: Int?,
    val fats: Int?,
    val carbs: Int?
)