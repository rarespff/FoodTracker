package com.example.licenta2024.page.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.licenta2024.data.DetailedRecipe
import com.example.licenta2024.data.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailedRecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ViewModel() {
    private val _detailedRecipe = MutableLiveData<DetailedRecipe>()
    val detailedRecipe: LiveData<DetailedRecipe>
        get() = _detailedRecipe

    private suspend fun getDetailedRecipeByID(recipeId: Int): DetailedRecipe {
        return withContext(Dispatchers.IO) {
            try {
                recipeRepository.getDetailedRecipeById(recipeId)
            } catch (e: Exception) {
                // Handle exceptions, if any
                Log.e("getDetail", "Error fetching details: ${e.message}")
                throw e // Rethrow the exception to propagate it further if needed
            }
        }
    }

    fun fetchDetailedRecipe(recipeId: Int) {
        viewModelScope.launch {
            try {
                val recipe = getDetailedRecipeByID(recipeId)
                _detailedRecipe.postValue(recipe)
            } catch (e: Exception) {
                // Handle exceptions
                Log.e("fetchRecipe", "Error fetching recipe: ${e.message}")
            }
        }
    }
}