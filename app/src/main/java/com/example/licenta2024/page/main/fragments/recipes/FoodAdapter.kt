package com.example.licenta2024.page.main.fragments.recipes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.licenta2024.R
import com.example.licenta2024.data.SearchedFood

class FoodAdapter(
    private val foods: MutableList<SearchedFood>,
    private val buttonClickHandler: (Int) -> Unit
) :
    RecyclerView.Adapter<FoodAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.food_rv_item, parent, false)
        return ViewHolder(view, buttonClickHandler)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = foods[position]
        holder.titleTextView.text = food.title
        holder.caloriesTextView.text = food.calories
        holder.proteinView.text = food.protein
        holder.fatsView.text = food.fats
        holder.carbsView.text = food.carbs
    }

    override fun getItemCount(): Int {
        return foods.size
    }

    fun updateFoods(newFoods: List<SearchedFood>) {
        foods.clear()
        foods.addAll(newFoods)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View, private val onClick: (Int) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val caloriesTextView: TextView = itemView.findViewById(R.id.caloriesTextView)
        val proteinView: TextView = itemView.findViewById(R.id.proteinTextView)
        val fatsView: TextView = itemView.findViewById(R.id.fatsTextView)
        val carbsView: TextView = itemView.findViewById(R.id.carbsTextView)
        val button: Button = itemView.findViewById(R.id.add_food)

        init {
            button.setOnClickListener {
                // Pass the adapter position to the click handler
                onClick(adapterPosition)
            }
        }
    }
}