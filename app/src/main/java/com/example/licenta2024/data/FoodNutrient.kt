package com.example.licenta2024.data

import com.squareup.moshi.Json

data class FoodNutrient(
    @Json(name = "nutrientName") val name: String?,
    @Json(name = "value") val value: Double?,
    @Json(name = "unitName") val unit: String?
)