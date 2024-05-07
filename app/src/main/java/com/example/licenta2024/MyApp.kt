package com.example.licenta2024

import android.app.Application
import com.example.licenta2024.data.DatabaseManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DatabaseManager.initialize(this)
    }
}
