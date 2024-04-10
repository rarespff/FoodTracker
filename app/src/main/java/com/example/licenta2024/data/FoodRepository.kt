package com.example.licenta2024.data

interface FoodRepository {
    suspend fun searchFood(query: String): String
}