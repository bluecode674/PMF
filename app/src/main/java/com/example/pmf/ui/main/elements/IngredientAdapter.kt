package com.example.pmf.ui.main.elements

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pmf.Ingredient
import com.example.pmf.R

class IngredientAdapter(private val ingredientList: List<Ingredient>) : RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_ingredient, parent, false)
        return IngredientViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val currentItem = ingredientList[position]
        holder.nameTextView.text = currentItem.name
        holder.purchaseDateTextView.text = currentItem.purchaseDate
        holder.expiryDateTextView.text = currentItem.expiryDate
        holder.storageLocationTextView.text = currentItem.storageLocation
    }

    override fun getItemCount() = ingredientList.size

    class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val purchaseDateTextView: TextView = itemView.findViewById(R.id.purchaseDateTextView)
        val expiryDateTextView: TextView = itemView.findViewById(R.id.expiryDateTextView)
        val storageLocationTextView: TextView = itemView.findViewById(R.id.storageLocationTextView)
    }
}
