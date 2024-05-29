package com.example.licenta2024.page.main.fragments.recipes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.licenta2024.R
import com.example.licenta2024.data.SearchedFoodResult

class FoodAdapter(
    private val foods: MutableList<SearchedFoodResult>,
    private val buttonClickHandler: (Int) -> Unit
) :
    RecyclerView.Adapter<FoodAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.food_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = foods[position]
        holder.titleTextView.text = food.title
        holder.caloriesTextView.text = food.calories.toString()
        holder.proteinView.text = food.protein.toString()
        holder.fatsView.text = food.fats.toString()
        holder.carbsView.text = food.carbs.toString()
        holder.button.setOnClickListener {
            buttonClickHandler(position)
        }
    }

    override fun getItemCount(): Int {
        return foods.size
    }


    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val caloriesTextView: TextView = itemView.findViewById(R.id.caloriesTextView)
        val proteinView: TextView = itemView.findViewById(R.id.proteinTextView)
        val fatsView: TextView = itemView.findViewById(R.id.fatsTextView)
        val carbsView: TextView = itemView.findViewById(R.id.carbsTextView)
        val button: Button = itemView.findViewById(R.id.add_food)
    }
}