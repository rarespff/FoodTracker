package com.example.licenta2024.data

import com.google.gson.annotations.SerializedName

data class DetailedRecipe(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("readyInMinutes")
    val readyInMinutes: Int?,
    @SerializedName("servings")
    val servings: Int?,
    @SerializedName("sourceUrl")
    val sourceUrl: String?,
    @SerializedName("nutrition")
    val nutrition: Nutrition
)