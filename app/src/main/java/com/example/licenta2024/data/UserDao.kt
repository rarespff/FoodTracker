package com.example.licenta2024.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert
    suspend fun addUser(user: User)

    @Query("SELECT * FROM User WHERE userId = :userId")
    fun getUser(userId: Long): LiveData<User>

    @Update
    suspend fun updateUser(user: User)
}