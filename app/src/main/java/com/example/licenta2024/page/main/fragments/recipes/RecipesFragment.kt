package com.example.licenta2024.page.main.fragments.recipes

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.licenta2024.R
import com.example.licenta2024.data.DatabaseManager
import com.example.licenta2024.data.Day
import com.example.licenta2024.data.Food
import com.example.licenta2024.data.Recipe
import com.example.licenta2024.data.SearchedFoodResult
import com.example.licenta2024.data.User
import com.example.licenta2024.page.main.MainActivity
import com.example.licenta2024.page.main.MainViewModel
import com.example.licenta2024.page.main.fragments.home.DayItem
import com.example.licenta2024.page.recipe.DetailedRecipeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RecipesFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var gridAdapter: RecipesAdapter
    private lateinit var foodAdapter: FoodAdapter
    private lateinit var searchView: SearchView
    private lateinit var recipesSearch: Button
    private lateinit var foodsSearch: Button
    private var foodList: MutableList<SearchedFoodResult> = mutableListOf()
    private var searchRecipes = true
    private lateinit var currentUser: User
    private var userId = -1L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userId = getCurrentUserId()
        getCurrentUser()
        val view = inflater.inflate(R.layout.recipes_fragment, container, false)
        searchView = view.findViewById(R.id.search_view)
        recyclerView = view.findViewById(R.id.recipes_rv)
        recipesSearch = view.findViewById(R.id.recipes_search)
        foodsSearch = view.findViewById(R.id.foods_search)
        var currentRecipes = mutableListOf<Recipe>()
        recipesSearch.setOnClickListener {
            searchRecipes = true
            searchView.queryHint = "Search recipes..."
            recyclerView.adapter = RecipesAdapter(currentRecipes) { recipe ->
                startActivity(
                    Intent(
                        requireContext(),
                        DetailedRecipeActivity::class.java
                    ).putExtra("RECIPE", recipe.id)
                )
            }
            recipesSearch.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.light_gray))
            foodsSearch.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.gray))
        }
        foodsSearch.setOnClickListener {
            searchRecipes = false
            recyclerView.adapter = FoodAdapter(foodList) { foodIndex ->
                handleFoodAdding(view, foodIndex)
            }
            foodsSearch.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.light_gray))
            recipesSearch.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.gray))
            searchView.queryHint = "Search foods..."
        }
        recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2) // Set your desired number of columns
        gridAdapter = RecipesAdapter(mutableListOf()) { recipe ->
            startActivity(
                Intent(
                    requireContext(),
                    DetailedRecipeActivity::class.java
                ).putExtra("RECIPE", recipe.id)
            )
        }
        foodAdapter = FoodAdapter(foodList) {}

        recyclerView.adapter = gridAdapter
        viewModel.recipeListLiveData.observe(viewLifecycleOwner) { newRecipes ->
            if (newRecipes != null) {
                currentRecipes = newRecipes as MutableList<Recipe>
                recyclerView.adapter = RecipesAdapter(newRecipes) { recipe ->
                    startActivity(
                        Intent(
                            requireContext(),
                            DetailedRecipeActivity::class.java
                        ).putExtra("RECIPE", recipe.id)
                    )
                }
            }
        }
        viewModel.foodListLiveData.observe(viewLifecycleOwner) { newFoods ->
            if (newFoods != null) {
                foodList = newFoods as MutableList<SearchedFoodResult>
                recyclerView.adapter = FoodAdapter(foodList) { foodIndex ->
                    handleFoodAdding(view, foodIndex)
                }
            }
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (searchRecipes) {
                    viewModel.getRecipesByName(query)
                } else {
                    viewModel.searchFoodByName(query)
                }
                searchView.setQuery("", false)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Handle search text change (optional)
                return false
            }
        })

        return view
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
                    0,
                    0,
                    0,
                    0,
                    0,
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
            requireContext(),
            MainActivity::class.java
        )
        startActivity(intent)
    }

    private fun getNewFoodObject(quantity: Double, index: Int): Food {
        val decimalFormat = DecimalFormat("#.#")
        val protein = foodList[index].protein?.let {
            decimalFormat.format(it * quantity).toDouble().toInt()
        } ?: 0
        val fats = foodList[index].fats?.let {
            decimalFormat.format(it * quantity).toDouble().toInt()
        } ?: 0
        val carbs = foodList[index].carbs?.let {
            decimalFormat.format(it * quantity).toDouble().toInt()
        } ?: 0
        val calories = foodList[index].calories?.let {
            decimalFormat.format(it * quantity).toDouble().toInt()
        } ?: 0

        val title = foodList[index].title ?: ""
        return Food(
            0,
            title,
            calories,
            protein,
            carbs,
            fats,
            (quantity * 100).toInt()
        )
    }


    private fun handleFoodAdding(view: View, index: Int) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_food, null)
        val editTextQuantity = dialogView.findViewById<EditText>(R.id.edit_text_quantity)
        val spinnerMeal = dialogView.findViewById<Spinner>(R.id.spinner_meal)
        val mealsArray = resources.getStringArray(R.array.meals_array)
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mealsArray)
        spinnerMeal.adapter = adapter
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)
            .setTitle("Add Food")
            .setPositiveButton("Add") { _, _ ->
                var quantity = editTextQuantity.text.toString().toDoubleOrNull() ?: 0.0
                quantity /= 100.0
                val selectedMeal = spinnerMeal.selectedItem as String
                when (selectedMeal) {
                    mealsArray[0] -> {
                        handleMeal(currentUser, getCurrentDayItem().dateId) { day ->
                            val currentBreakfast = day.breakfast.toMutableList()
                            val newFood = getNewFoodObject(quantity, index)
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
                            val newFood = getNewFoodObject(quantity, index)
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
                            val newFood = getNewFoodObject(quantity, index)
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
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun getCurrentUser() {
        DatabaseManager.getUser(userId).observe(viewLifecycleOwner) { user ->
            currentUser = user
        }
    }

    private fun getCurrentUserId(): Long {
        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userIdString: String? = sharedPreferences.getString("userId", null)
        return userIdString?.toLongOrNull() ?: -1L
    }

    private fun getCurrentDayItem(): DayItem {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("E", Locale.getDefault())
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        val currentYear = calendar.get(Calendar.YEAR)
        val currentDayName = dateFormat.format(calendar.time)
        val currentDateId = "$currentDay${currentMonth.toString().padStart(2, '0')}$currentYear"
        return DayItem(
            currentDateId,
            currentDay.toString(),
            currentDayName,
            currentMonth.toString()
        )
    }
}