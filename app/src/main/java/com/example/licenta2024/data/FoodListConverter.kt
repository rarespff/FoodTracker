package com.example.licenta2024.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FoodListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromFoodList(foods: List<Food>?): String? {
        return gson.toJson(foods)
    }

    @TypeConverter
    fun toFoodList(foodsJson: String?): List<Food>? {
        val listType = object : TypeToken<List<Food>>() {}.type
        return gson.fromJson(foodsJson, listType)
    }
}
