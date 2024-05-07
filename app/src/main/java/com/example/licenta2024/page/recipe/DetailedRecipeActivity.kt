package com.example.licenta2024.page.recipe

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.licenta2024.R
import com.example.licenta2024.data.DatabaseManager
import com.example.licenta2024.data.DetailedRecipe
import com.example.licenta2024.data.Food
import com.example.licenta2024.page.main.fragments.home.DayItem
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
    private lateinit var currentRecipe: DetailedRecipe
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_recipe)
        handleRecipeData()
        handleFoodAdding()
    }

    private fun handleFoodAdding() {
        val addToJournalButton = findViewById<Button>(R.id.add_to_journal)
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_food, null)
        val editTextQuantity = dialogView.findViewById<EditText>(R.id.edit_text_quantity)
        val spinnerMeal = dialogView.findViewById<Spinner>(R.id.spinner_meal)
        val mealsArray = resources.getStringArray(R.array.meals_array)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mealsArray)
        spinnerMeal.adapter = adapter
        addToJournalButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setView(dialogView)
                .setTitle("Add Food")
                .setPositiveButton("Add") { dialog, which ->
                    var quantity = editTextQuantity.text.toString().toDoubleOrNull() ?: 0.0
                    quantity /= 1000.0
                    val selectedMeal = spinnerMeal.selectedItem as String
                    Log.e("dialog:", quantity.toString())
                    Log.e("dialog:", selectedMeal)

                    // Launch a coroutine in the IO context to perform the database operation
                    DatabaseManager.getUser(1L).observe(this@DetailedRecipeActivity) { user ->
                        // Check if user is not null before proceeding
                        user ?: return@observe

                        when (selectedMeal) {
                            mealsArray[0] -> {
                                var currentBreakfast =
                                    user.days.find { it.dateId == getCurrentDayItem().dateId }?.breakfast as MutableList?
                                if (currentBreakfast == null) {
                                    currentBreakfast = mutableListOf()
                                }
                                currentBreakfast.add(getNewFoodObject(quantity))
                                user.days.find { it.dateId == getCurrentDayItem().dateId }?.breakfast =
                                    currentBreakfast
                            }

                            mealsArray[1] -> {
                                var currentLunch =
                                    user.days.find { it.dateId == getCurrentDayItem().dateId }?.lunch as MutableList?
                                if (currentLunch == null) {
                                    currentLunch = mutableListOf()
                                }
                                currentLunch.add(getNewFoodObject(quantity))
                                user.days.find { it.dateId == getCurrentDayItem().dateId }?.lunch =
                                    currentLunch
                            }

                            mealsArray[2] -> {
                                var currentDinner =
                                    user.days.find { it.dateId == getCurrentDayItem().dateId }?.dinner as MutableList?
                                if (currentDinner == null) {
                                    currentDinner = mutableListOf()
                                }
                                currentDinner.add(getNewFoodObject(quantity))
                                user.days.find { it.dateId == getCurrentDayItem().dateId }?.dinner =
                                    currentDinner
                            }
                        }
                        Log.e("newUser", user.toString())
                        CoroutineScope(Dispatchers.IO).launch {
                            DatabaseManager.updateUser(user)
                        }

                    }
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun getNewFoodObject(quantity: Double): Food {
        var protein = currentRecipe.nutrition.nutrients[8].amount ?: 0.0
        protein *= quantity
        var fats = currentRecipe.nutrition.nutrients[1].amount ?: 0.0
        fats *= quantity
        var carbs = currentRecipe.nutrition.nutrients[3].amount ?: 0.0
        carbs *= quantity
        var calories = currentRecipe.nutrition.nutrients[0].amount ?: 0.0
        calories *= quantity
        val title = currentRecipe.title ?: ""
        return Food(
            0,
            title,
            calories,
            protein,
            carbs,
            fats,
            quantity
        )
    }

    private fun handleRecipeData() {
        recipeViewModel.detailedRecipe.observe(this) { detailedRecipe ->
            currentRecipe = detailedRecipe
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

    private fun getCurrentDayItem(): DayItem {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("E", Locale.getDefault())

        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentMonth = calendar.get(Calendar.MONTH) + 1 // Month is 0-based, so add 1
        val currentYear = calendar.get(Calendar.YEAR)

        val currentDayName = dateFormat.format(calendar.time)

        // Generate the dateId for the current day in the format DDMMYYYY
        val currentDateId = "$currentDay${currentMonth.toString().padStart(2, '0')}$currentYear"

        // Create and return a DayItem object for the current day
        return DayItem(
            currentDateId,
            currentDay.toString(),
            currentDayName,
            currentMonth.toString()
        )
    }
}