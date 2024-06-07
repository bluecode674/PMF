package com.example.pmf.ui.recipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pmf.DB.Recipe
import com.example.pmf.R

class RecipeAdapter(private var recipes: List<Recipe>, private val onRecipeClick: (Int) -> Unit) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return RecipeViewHolder(view, onRecipeClick)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe)
    }

    override fun getItemCount(): Int = recipes.size

    fun updateRecipes(newRecipes: List<Recipe>) {
        recipes = newRecipes
        notifyDataSetChanged()
    }

    class RecipeViewHolder(itemView: View, private val onRecipeClick: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val tvRecipeName: TextView = itemView.findViewById(R.id.tvRecipeName)
        private val tvRecipeIngredients: TextView = itemView.findViewById(R.id.tvRecipeIngredients)

        fun bind(recipe: Recipe) {
            tvRecipeName.text = recipe.name
            tvRecipeIngredients.text = "필요한 재료: ${recipe.ingredients.joinToString(", ")}"
            itemView.setOnClickListener {
                onRecipeClick(recipe.id)
            }
        }
    }
}
