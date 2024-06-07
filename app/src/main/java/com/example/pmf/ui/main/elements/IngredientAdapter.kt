package com.example.pmf.ui.main.elements

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pmf.DB.Ingredient
import com.example.pmf.R

class IngredientAdapter(
    private var ingredientList: List<Ingredient>,
    private val onItemClicked: (Ingredient) -> Unit
) : RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ingredient, parent, false)
        return IngredientViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val ingredient = ingredientList[position]
        holder.bind(ingredient)
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }

    fun updateList(newList: List<Ingredient>) {
        ingredientList = newList
        notifyDataSetChanged()
    }

    class IngredientViewHolder(itemView: View, private val onItemClicked: (Ingredient) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val tvIngredientName: TextView = itemView.findViewById(R.id.tvIngredientName)
        private val tvExpiryDate: TextView = itemView.findViewById(R.id.tvExpiryDate)
        private val tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        private val tvRemainingDays: TextView = itemView.findViewById(R.id.tvRemainingDays)

        fun bind(ingredient: Ingredient) {
            tvIngredientName.text = ingredient.name
            tvExpiryDate.text = ingredient.expiryDate
            tvQuantity.text = "Quantity: ${ingredient.quantity}"
            tvRemainingDays.text = "D-Day: ${ingredient.getRemainingDays()}"

            itemView.setOnClickListener {
                onItemClicked(ingredient)
            }
        }
    }
}
