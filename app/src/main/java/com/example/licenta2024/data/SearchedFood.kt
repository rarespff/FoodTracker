package com.example.licenta2024.data

import com.squareup.moshi.Json

data class SearchedFood(
    @Json(name = "fdcId") val id: Int?,
    @Json(name = "description") val title: String?,
    @Json(name = "foodNutrients") val nutrients: List<FoodNutrient>?
)