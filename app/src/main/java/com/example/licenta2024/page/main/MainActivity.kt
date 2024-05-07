package com.example.licenta2024.page.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.licenta2024.R
import com.example.licenta2024.data.DatabaseManager
import com.example.licenta2024.data.Day
import com.example.licenta2024.data.Goals
import com.example.licenta2024.data.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var bottomNavigationView: BottomNavigationView
    private val viewPager by lazy { findViewById<ViewPager2>(R.id.viewPager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager.isUserInputEnabled = false
        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_item_fragment_one -> viewPager.currentItem = 0
                R.id.menu_item_fragment_two -> viewPager.currentItem = 1
                R.id.menu_item_fragment_three -> viewPager.currentItem = 2
            }
            true
        }
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bottomNavigationView.menu.getItem(position).isChecked = true
            }
        })
        viewModel.recipeListLiveData.observe(this) {}
        CoroutineScope(Dispatchers.IO).launch { addMockUser() }
    }

    private suspend fun addMockUser() {
        val user = User(
            email = "example@example.com",
            firstName = "John",
            lastName = "Doe",
            password = "password",
            age = 30,
            weight = 70.0,
            height = 175.0,
            image = null,
            goals = Goals(
                calories = 2002.0,
                protein = 103.0,
                carbs = 201.0,
                fats = 52.0,
                stepGoal = 10001,
                waterIntakeGoal = 3000
            ),
            days = listOf(
                Day(
                    dateId = "3052024",
                    dayNumber = "1",
                    dayName = "Monday",
                    dayMonth = "May",
                    totalConsumedCalories = 2000.0,
                    totalBurnedCalories = 1500.0,
                    proteinIntake = 100.0,
                    carbsIntake = 200.0,
                    fatsIntake = 50.0,
                    waterIntake = 2000,
                    steps = 8000,
                    breakfast = listOf(
                    ),
                    lunch = listOf(
                    ),
                    dinner = listOf(
                    )
                )
            )
        )
        DatabaseManager.addUser(user)
    }

    fun navigateToRecipesFragment() {
        viewPager.currentItem = 1
    }
}
