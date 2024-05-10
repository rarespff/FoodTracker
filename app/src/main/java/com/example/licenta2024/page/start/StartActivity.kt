package com.example.licenta2024.page.start

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.licenta2024.R
import com.example.licenta2024.page.main.MainActivity
import com.example.licenta2024.page.start.fragments.LoginFragment

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_activity)
        if (getCurrentUserId() != -1L) {
            startMainActivity()
        }
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container, LoginFragment())
            }
        }
    }

    private fun getCurrentUserId(): Long {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userIdString: String? = sharedPreferences.getString("userId", null)
        return userIdString?.toLongOrNull() ?: -1L
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}