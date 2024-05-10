package com.example.licenta2024.page.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.licenta2024.R
import com.example.licenta2024.page.start.StartActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private val viewPager by lazy { findViewById<ViewPager2>(R.id.viewPager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getCurrentUserId() == -1L) {
            goToLogin()
        }
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
    }

    private fun getCurrentUserId(): Long {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userIdString: String? = sharedPreferences.getString("userId", null)
        return userIdString?.toLongOrNull() ?: -1L
    }

    private fun goToLogin() {
        val intent = Intent(this, StartActivity::class.java)
        startActivity(intent)
    }

    fun navigateToRecipesFragment() {
        viewPager.currentItem = 1
    }
}
