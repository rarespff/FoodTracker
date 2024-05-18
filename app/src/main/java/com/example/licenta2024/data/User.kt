package com.example.licenta2024.data

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Long = 0,
    val email: String,
    val firstName: String,
    val lastName: String,
    val password: String,
    val age: Int,
    val weight: Int,
    val height: Int,
    val activityLevel: String,
    val gender: String,
    var image: Bitmap?,
    var goals: Goals?,
    var days: List<Day>
)