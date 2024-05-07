package com.example.licenta2024.data

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
    val weight: Double,
    val height: Double,
    val image: String?,
    val goals: Goals?,
    val days: List<Day>
)