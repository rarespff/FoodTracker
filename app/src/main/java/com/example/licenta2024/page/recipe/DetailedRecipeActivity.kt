package com.example.licenta2024.page.recipe

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.licenta2024.R
import com.example.licenta2024.data.DetailedRecipe
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailedRecipeActivity : AppCompatActivity() {
    private val recipeViewModel: DetailedRecipeViewModel by viewModels()
    private val recipeImage by lazy { findViewById<ImageView>(R.id.recipe_photo) }
    private val recipeTitle by lazy { findViewById<TextView>(R.id.recipe_title) }
    private val portionsCount by lazy { findViewById<TextView>(R.id.portions_count) }
    private val cookingTime by lazy { findViewById<TextView>(R.id.timer_value) }
    private val proteinValue by lazy { findViewById<TextView>(R.id.protein_value) }
    private val carbsValue by lazy { findViewById<TextView>(R.id.carbs_value) }
    private val fatsValue by lazy { findViewById<TextView>(R.id.fats_value) }
    private val recipeCalories by lazy { findViewById<TextView>(R.id.recipe_calories) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_recipe)
        recipeViewModel.detailedRecipe.observe(this) { detailedRecipe ->
            showRecipe(detailedRecipe)
        }

        val recipeId = intent.getIntExtra("RECIPE", -1)
        if (recipeId != -1) {
            recipeViewModel.fetchDetailedRecipe(recipeId)
        } else {
            Toast.makeText(this, "Recipe not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showRecipe(detailedRecipe: DetailedRecipe) {
        Picasso.get().load(detailedRecipe.image).placeholder(R.drawable.placeholder)
            .into(recipeImage)
        recipeTitle.text = detailedRecipe.title
        portionsCount.text = detailedRecipe.servings.toString()
        cookingTime.text = detailedRecipe.readyInMinutes.toString()
        proteinValue.text = detailedRecipe.nutrition.nutrients[8].amount.toString()
        fatsValue.text = detailedRecipe.nutrition.nutrients[1].amount.toString()
        carbsValue.text = detailedRecipe.nutrition.nutrients[3].amount.toString()
        recipeCalories.text = detailedRecipe.nutrition.nutrients[0].amount.toString()
    }
}