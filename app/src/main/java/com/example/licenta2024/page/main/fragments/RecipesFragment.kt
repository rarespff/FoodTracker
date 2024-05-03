package com.example.licenta2024.page.main.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.licenta2024.R
import com.example.licenta2024.page.main.MainViewModel
import com.example.licenta2024.page.main.RecipesAdapter
import com.example.licenta2024.page.recipe.DetailedRecipeActivity

class RecipesFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var gridAdapter: RecipesAdapter
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.recipes_fragment, container, false)
        searchView = view.findViewById(R.id.search_view)
        recyclerView = view.findViewById(R.id.recipes_rv)
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

        recyclerView.adapter = gridAdapter
        viewModel.recipeListLiveData.observe(viewLifecycleOwner) { newRecipes ->
            if (newRecipes != null) {
                gridAdapter.updateRecipes(newRecipes)
            }
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.getRecipesByName(query)
                searchView.setQuery("",false)
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