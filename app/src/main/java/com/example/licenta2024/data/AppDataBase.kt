package com.example.licenta2024.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [User::class, Day::class, Food::class, Goals::class], version = 1)
@TypeConverters(value = [GoalsConverter::class, FoodListConverter::class, DayListConverter::class, ProfilePictureConverter::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun dayDao(): DayDao
}
