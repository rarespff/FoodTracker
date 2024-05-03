package com.example.licenta2024.page.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.licenta2024.data.Recipe
import com.example.licenta2024.data.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    val recipeListLiveData = MutableLiveData<List<Recipe>?>()

    init {
        getRandomRecipes()
    }

    private fun getRandomRecipes() {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    recipeRepository.getRandomRecipeList()
                }
                recipeListLiveData.postValue(response)
                Log.e("element", response.toString())
            } catch (e: Exception) {
                // Handle exceptions, if any
                Log.e("getDetail", "Error fetching details: ${e.message}")
            }
        }
    }

    fun getRecipesByName(recipeName: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    recipeRepository.getRecipesByName(recipeName)
                }
                recipeListLiveData.postValue(response)
                Log.e("element", response.toString())
            } catch (e: Exception) {
                // Handle exceptions, if any
                Log.e("getDetail", "Error fetching details: ${e.message}")
            }
        }
    }
}
