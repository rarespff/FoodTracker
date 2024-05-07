package com.example.licenta2024.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DayDao {
    @Insert
    suspend fun addDay(day: Day)

    @Query("SELECT * FROM Day WHERE dateId = :dateId")
    fun getDay(dateId: Long): LiveData<Day?>
}