package com.example.licenta2024.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class FoodApiClient {
    private val client = OkHttpClient()
    private val apiKey = "5aXLjsdO5XMEjtL7WYD6sd6ayMRHKahxS3p9Pc5y"
    private val baseUrl = "https://api.nal.usda.gov/fdc/v1/foods/search"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val foodSearchResponseAdapter = moshi.adapter(FoodSearchResponse::class.java)

    suspend fun searchFood(query: String): List<SearchedFoodResult>? = withContext(Dispatchers.IO) {
        val url = "$baseUrl?api_key=$apiKey&query=${query.replace(" ", "%20")}"
        val request = Request.Builder()
            .url(url)
            .build()

        return@withContext try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                response.body?.let { responseBody ->
                    val foodSearchResponse = foodSearchResponseAdapter.fromJson(responseBody.string())
                    foodSearchResponse?.foods?.map { food ->
                        val nutrients = food.nutrients?.associateBy { it.name }
                        SearchedFoodResult(
                            id = food.id,
                            title = food.title,
                            calories = nutrients?.get("Energy")?.value?.toString(),
                            protein = nutrients?.get("Protein")?.value?.toString(),
                            fats = nutrients?.get("Total lipid (fat)")?.value?.toString(),
                            carbs = nutrients?.get("Carbohydrate, by difference")?.value?.toString()
                        )
                    }
                }
            } else {
                null
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}