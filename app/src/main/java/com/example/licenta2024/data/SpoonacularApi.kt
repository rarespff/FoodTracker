package com.example.licenta2024.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface SpoonacularApi {

    @GET("complexSearch")
    suspend fun getRecipesByName(@QueryMap queries: Map<String, String>): QueryResponse

    @GET("random")
    suspend fun getRandomRecipes(@QueryMap queries: Map<String, String>): RandomResponse

    @GET("{id}/information")
    suspend fun getDetailedRecipeById(
        @Path("id") recipeId: String,
        @QueryMap queries: Map<String, String>
    ): DetailedRecipe
}