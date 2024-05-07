package com.example.licenta2024.page.main.fragments.recipes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.licenta2024.R
import com.example.licenta2024.data.Recipe
import com.squareup.picasso.Picasso

class RecipesAdapter(private val recipes: MutableList<Recipe>, private val onItemClick: (Recipe) -> Unit) :
    RecyclerView.Adapter<RecipesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipes[position]

        // Load image using Picasso library
        Picasso.get().load(recipe.image).placeholder(R.drawable.placeholder).into(holder.imageView)

        holder.titleTextView.text = recipe.title

        // Set click listener
        holder.itemView.setOnClickListener {
            onItemClick.invoke(recipe)
        }
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun updateRecipes(newRecipes: List<Recipe>) {
        recipes.clear()
        recipes.addAll(newRecipes)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
    }
}