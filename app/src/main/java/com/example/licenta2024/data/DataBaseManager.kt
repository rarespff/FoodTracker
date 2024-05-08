package com.example.licenta2024.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room

object DatabaseManager {
    private lateinit var database: AppDatabase

    fun initialize(context: Context) {
        database = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "nutrition-db"
        ).build()
    }

    suspend fun addUser(user: User) {
        database.userDao().addUser(user)
    }

    fun getUser(userId: Long): LiveData<User> {
        return database.userDao().getUser(userId)
    }

    suspend fun updateUser(user: User) {
        database.userDao().updateUser(user)
    }
}