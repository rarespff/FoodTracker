package com.example.licenta2024.page.recipe

import android.content.Intent
import android.os.Bundle
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
import com.example.licenta2024.data.Day
import com.example.licenta2024.data.DetailedRecipe
import com.example.licenta2024.data.Food
import com.example.licenta2024.data.User
import com.example.licenta2024.page.main.MainActivity
import com.example.licenta2024.page.main.fragments.home.DayItem
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat
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
    private lateinit var currentUser: User
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
                    when (selectedMeal) {
                        mealsArray[0] -> {
                            // Breakfast case
                            handleMeal(currentUser, getCurrentDayItem().dateId) { day ->
                                val currentBreakfast = day.breakfast.toMutableList()
                                val newFood = getNewFoodObject(quantity)
                                currentBreakfast.add(newFood)
                                day.totalConsumedCalories += newFood.calories
                                day.proteinIntake += newFood.protein
                                day.carbsIntake += newFood.carbs
                                day.fatsIntake += newFood.fats
                                day.copy(breakfast = currentBreakfast)
                            }
                        }

                        mealsArray[1] -> {
                            // Lunch case
                            handleMeal(currentUser, getCurrentDayItem().dateId) { day ->
                                val currentLunch = day.lunch.toMutableList()
                                val newFood = getNewFoodObject(quantity)
                                currentLunch.add(newFood)
                                day.totalConsumedCalories += newFood.calories
                                day.proteinIntake += newFood.protein
                                day.carbsIntake += newFood.carbs
                                day.fatsIntake += newFood.fats
                                day.copy(lunch = currentLunch)
                            }
                        }

                        mealsArray[2] -> {
                            // Dinner case
                            handleMeal(currentUser, getCurrentDayItem().dateId) { day ->
                                val currentDinner = day.dinner.toMutableList()
                                val newFood = getNewFoodObject(quantity)
                                currentDinner.add(newFood)
                                day.totalConsumedCalories += newFood.calories
                                day.proteinIntake += newFood.protein
                                day.carbsIntake += newFood.carbs
                                day.fatsIntake += newFood.fats
                                day.copy(dinner = currentDinner)
                            }
                        }
                    }
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private inline fun handleMeal(
        user: User,
        dateId: String,
        modifyDay: (Day) -> Day
    ) {
        val currentUserDays = user.days.toMutableList()
        val currentDay = currentUserDays.find { it.dateId == dateId }
            ?: getCurrentDayItem().let {
                Day(
                    it.dateId,
                    it.day,
                    it.dayName,
                    it.dayMonth,
                    0.0,
                    0.0,
                    0.0,
                    0.0,
                    0.0,
                    0,
                    listOf(),
                    listOf(),
                    listOf(),
                    0
                )
            }
        val updatedDay = modifyDay(currentDay)
        val index = currentUserDays.indexOfFirst { it.dateId == dateId }
        if (index != -1) {
            currentUserDays[index] = updatedDay
        } else {
            currentUserDays.add(updatedDay)
        }
        user.days = currentUserDays
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseManager.updateUser(user)
        }
        val intent = Intent(
            this,
            MainActivity::class.java
        )
        startActivity(intent)
    }

    private fun getNewFoodObject(quantity: Double): Food {
        val decimalFormat = DecimalFormat("#.#")

        var protein = currentRecipe.nutrition.nutrients[8].amount?.let {
            decimalFormat.format(it * quantity).toDouble()
        } ?: 0.0
        var fats = currentRecipe.nutrition.nutrients[1].amount?.let {
            decimalFormat.format(it * quantity).toDouble()
        } ?: 0.0
        var carbs = currentRecipe.nutrition.nutrients[3].amount?.let {
            decimalFormat.format(it * quantity).toDouble()
        } ?: 0.0
        var calories = currentRecipe.nutrition.nutrients[0].amount?.let {
            decimalFormat.format(it * quantity).toDouble()
        } ?: 0.0

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

    private fun getCurrentUser() {
        DatabaseManager.getUser(1L).observe(this) { user ->
            currentUser = user
        }
    }

    private fun handleRecipeData() {
        recipeViewModel.detailedRecipe.observe(this) { detailedRecipe ->
            currentRecipe = detailedRecipe
            showRecipe(detailedRecipe)
        }
        getCurrentUser()
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