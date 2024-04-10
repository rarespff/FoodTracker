package com.example.licenta2024.page.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.licenta2024.R
import com.example.licenta2024.page.main.MainViewModel
import com.example.licenta2024.page.main.RecipesAdapter

class ExploreFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var gridAdapter: RecipesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.explore_fragment, container, false)
        recyclerView = view.findViewById(R.id.recipes_rv)
        recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2) // Set your desired number of columns
        gridAdapter = RecipesAdapter()
        recyclerView.adapter = gridAdapter
        return view
    }
}