package com.example.licenta2024.page.main.fragments.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.licenta2024.R
import com.example.licenta2024.data.Food

class FoodAdapter(private var foodList: List<Food>) :
    RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.meal_rv_item, parent, false)
        return FoodViewHolder(itemView)
    }

    fun updateData(newFoodList: List<Food>) {
        foodList = newFoodList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val currentItem = foodList[position]
        Log.e("holder:", currentItem.name)
        holder.bind(currentItem)
    }

    override fun getItemCount() = foodList.size

    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.name)
        private val quantityTextView: TextView = itemView.findViewById(R.id.quantity)
        private val caloriesTextView: TextView = itemView.findViewById(R.id.calories)
        private val proteinTextView: TextView = itemView.findViewById(R.id.protein)
        private val carbsTextView: TextView = itemView.findViewById(R.id.carbs)
        private val fatsTextView: TextView = itemView.findViewById(R.id.fats)
        fun bind(foodItem: Food) {
            nameTextView.text = foodItem.name
            quantityTextView.text = foodItem.quantity.toString()
            caloriesTextView.text = foodItem.calories.toString()
            proteinTextView.text = foodItem.protein.toString()
            carbsTextView.text = foodItem.carbs.toString()
            fatsTextView.text = foodItem.fats.toString()
        }
    }
}
