package com.example.licenta2024.page.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.licenta2024.data.FoodItem
import com.example.licenta2024.data.FoodRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val foodRepository: FoodRepositoryImpl
) : ViewModel() {

    val foodItemsLiveData = MutableLiveData<List<FoodItem>>()
    fun searchFood(query: String) {
        viewModelScope.launch {
            val result = foodRepository.searchFood(query)
            foodItemsLiveData.value = parseFoodItems(result)
        }
    }

    private fun parseFoodItems(response: String): List<FoodItem> {
        val foodItems = mutableListOf<FoodItem>()

        // Parse the JSON string
        val jsonObject = JSONObject(response)
        val foodsArray = jsonObject.getJSONArray("foods")

        // Iterate through each food item in the array
        for (i in 0 until foodsArray.length()) {
            val foodObject = foodsArray.getJSONObject(i)

            // Extract relevant fields
            val description = foodObject.getString("description")
            val fdcId = foodObject.getString("fdcId")
            val brandName = foodObject.optString("brandName", "")
            val packageWeight = foodObject.optString("packageWeight", "")

            // Extract food nutrients
            val foodNutrientsArray = foodObject.getJSONArray("foodNutrients")
            var calories = 0
            var protein = 0.0
            var fat = 0.0
            var carbohydrates = 0.0

            // Iterate through food nutrients
            for (j in 0 until foodNutrientsArray.length()) {
                val nutrientObject = foodNutrientsArray.getJSONObject(j)
                when (nutrientObject.getString("nutrientName")) {
                    "Protein" -> protein = nutrientObject.getDouble("value")
                    "Total lipid (fat)" -> fat = nutrientObject.getDouble("value")
                    "Carbohydrate, by difference" -> carbohydrates =
                        nutrientObject.getDouble("value")

                    "Energy" -> calories = nutrientObject.getInt("value")
                }
            }

            // Create a FoodItem instance and add it to the list
            val foodItem = FoodItem(
                description,
                fdcId,
                calories,
                protein,
                fat,
                carbohydrates,
                brandName,
                packageWeight
            )
            foodItems.add(foodItem)
        }

        return foodItems
    }
}
