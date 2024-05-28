package com.example.licenta2024.data

import com.squareup.moshi.Json

data class SearchedFoodResult(
    val id: Int?,
    val title: String?,
    val calories: String?,
    val protein: String?,
    val fats: String?,
    val carbs: String?
)