package com.example.licenta2024.page.main.fragments.recipes

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.licenta2024.R
import com.example.licenta2024.data.Recipe
import com.example.licenta2024.data.SearchedFoodResult
import com.example.licenta2024.page.main.MainViewModel
import com.example.licenta2024.page.recipe.DetailedRecipeActivity

class RecipesFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var gridAdapter: RecipesAdapter
    private lateinit var foodAdapter: FoodAdapter
    private lateinit var searchView: SearchView
    private lateinit var recipesSearch: Button
    private lateinit var foodsSearch: Button
    private var foodList: MutableList<SearchedFoodResult> = mutableListOf()
    private var searchRecipes = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.recipes_fragment, container, false)
        searchView = view.findViewById(R.id.search_view)
        recyclerView = view.findViewById(R.id.recipes_rv)
        recipesSearch = view.findViewById(R.id.recipes_search)
        foodsSearch = view.findViewById(R.id.foods_search)
        var currentRecipes = mutableListOf<Recipe>()
        recipesSearch.setOnClickListener {
            searchRecipes = true
            searchView.queryHint = "Search recipes..."
            recyclerView.adapter = RecipesAdapter(currentRecipes) { recipe ->
                startActivity(
                    Intent(
                        requireContext(),
                        DetailedRecipeActivity::class.java
                    ).putExtra("RECIPE", recipe.id)
                )
            }
            recipesSearch.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.light_gray))
            foodsSearch.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.gray))
        }
        foodsSearch.setOnClickListener {
            searchRecipes = false
            recyclerView.adapter = FoodAdapter(foodList) {}
            foodsSearch.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.light_gray))
            recipesSearch.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.gray))
            searchView.queryHint = "Search foods..."
        }
        recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2) // Set your desired number of columns
        gridAdapter = RecipesAdapter(mutableListOf()) { recipe ->
            startActivity(
                Intent(
                    requireContext(),
                    DetailedRecipeActivity::class.java
                ).putExtra("RECIPE", recipe.id)
            )
        }
        foodAdapter = FoodAdapter(foodList) {}

        recyclerView.adapter = gridAdapter
        viewModel.recipeListLiveData.observe(viewLifecycleOwner) { newRecipes ->
            if (newRecipes != null) {
                currentRecipes = newRecipes as MutableList<Recipe>
                recyclerView.adapter = RecipesAdapter(newRecipes) { recipe ->
                    startActivity(
                        Intent(
                            requireContext(),
                            DetailedRecipeActivity::class.java
                        ).putExtra("RECIPE", recipe.id)
                    )
                }
            }
        }
        viewModel.foodListLiveData.observe(viewLifecycleOwner) { newFoods ->
            if (newFoods != null) {
                foodList = newFoods as MutableList<SearchedFoodResult>
                recyclerView.adapter = FoodAdapter(foodList) {}
            }
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (searchRecipes) {
                    viewModel.getRecipesByName(query)
                } else {
                    viewModel.searchFoodByName(query)
                }
                searchView.setQuery("", false)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Handle search text change (optional)
                return false
            }
        })

        return view
    }
}