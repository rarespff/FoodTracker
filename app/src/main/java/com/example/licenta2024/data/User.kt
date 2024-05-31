package com.example.licenta2024.data

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Long = 0,
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val password: String = "",
    val age: Int = 0,
    val weight: Int = 0,
    val height: Int = 0,
    val activityLevel: String = "",
    val gender: String = "",
    var image: Bitmap? = null,
    var goals: Goals? = null,
    var days: List<Day> = emptyList()
) {
    // Default constructor
    constructor() : this(
        userId = 0,
        email = "",
        firstName = "",
        lastName = "",
        password = "",
        age = 0,
        weight = 0,
        height = 0,
        activityLevel = "",
        gender = "",
        image = null,
        goals = null,
        days = emptyList()
    )
}
