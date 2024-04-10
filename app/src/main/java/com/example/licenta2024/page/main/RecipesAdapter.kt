package com.example.licenta2024.page.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.licenta2024.R

class RecipesAdapter() :
    RecyclerView.Adapter<RecipesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recipe_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // val gridItem = gridItemList[position]

        holder.imageView.setImageResource(R.drawable.placeholder)
        holder.titleTextView.text = "Paste cu ton"
        holder.descriptionTextView.text = "800 kcal"
    }

    override fun getItemCount(): Int {
        return 20
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
    }
}