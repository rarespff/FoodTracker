package com.example.licenta2024.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL
import javax.inject.Inject

class FoodRepositoryImpl @Inject constructor() : FoodRepository {
    override suspend fun searchFood(query: String): String {
        return withContext(Dispatchers.IO) {
            val apiKey = "NP8mQ6L5cD1WAe0mKbevQOgdrOJr68at4wFpFyi1"
            val url = "https://api.nal.usda.gov/fdc/v1/foods/search?api_key=$apiKey&query=$query"

            try {
                val response = URL(url).readText()
                response
            } catch (e: IOException) {
                "Error: ${e.message}"
            }
        }
    }
}