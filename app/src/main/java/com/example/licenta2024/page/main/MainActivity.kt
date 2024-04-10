package com.example.licenta2024.page.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.licenta2024.R
import com.example.licenta2024.data.FoodItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
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
        viewModel.searchFood("chicken with rice")
        viewModel.foodItemsLiveData.observe(this){ foodItemsList->
            printFoodItems(foodItemsList)
        }

    }

    private fun printFoodItems(foodItems: List<FoodItem>) {
        // Print the list of FoodItem objects in a formatted way
        for (foodItem in foodItems) {
            println("Description: ${foodItem.description}")
            println("FDC ID: ${foodItem.fdcId}")
            println("Calories: ${foodItem.calories}")
            println("Protein: ${foodItem.protein}")
            println("Fat: ${foodItem.fat}")
            println("Carbohydrates: ${foodItem.carbohydrates}")
            println("Brand Name: ${foodItem.brandName}")
            println("Package Weight: ${foodItem.packageWeight}")
            println("-----------------------------------")
        }
    }
}
