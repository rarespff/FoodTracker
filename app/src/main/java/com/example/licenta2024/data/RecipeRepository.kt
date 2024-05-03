package com.example.licenta2024.data

import android.util.Log
import com.example.licenta2024.util.Constants
import javax.inject.Inject

class RecipeRepository @Inject constructor(private val api: SpoonacularApi) {

    suspend fun getRandomRecipeList(): List<Recipe> {
        val map = HashMap<String, String>()
        map["apiKey"] = Constants.ACCESS_TOKEN
        map["number"] = "10"
        val response = api.getRandomRecipes(map)
        Log.e("random", response.toString())
        return response.recipes?.map {
            Recipe(
                it.id,
                it.image,
                it.title
            )
        } ?: listOf()
    }

    suspend fun getRecipesByName(recipeName: String): List<Recipe> {
        val map = HashMap<String, String>()
        map["apiKey"] = Constants.ACCESS_TOKEN
        map["query"] = recipeName
        map["number"] = "10"
        val response = api.getRecipesByName(map)
        Log.e("byName", response.toString())
        return response.results?.map {
            Recipe(
                it.id,
                it.image,
                it.title
            )
        } ?: listOf()
    }

    suspend fun getDetailedRecipeById(recipeId: Int) : DetailedRecipe {
        val map = HashMap<String, String>()
        map["apiKey"] = Constants.ACCESS_TOKEN
        map["includeNutrition"] = "true"
        val response = api.getDetailedRecipeById(recipeId.toString(), map)
        Log.e("byId", response.toString())
        return response
    }
}