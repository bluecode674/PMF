package com.example.pmf.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pmf.DB.RecipeDBHelper
import com.example.pmf.databinding.FragmentRecipeDetailBinding

class RecipeDetailFragment : Fragment() {

    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var recipeDBHelper: RecipeDBHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipeDBHelper = RecipeDBHelper(requireContext())

        val recipeId = arguments?.getInt("recipeId") ?: return
        val recipe = recipeDBHelper.getRecipeById(recipeId) ?: return

        binding.tvRecipeName.text = recipe.name
        binding.tvRecipeIngredients.text = "필요한 재료: ${recipe.ingredients.joinToString(", ")}"
        binding.tvRecipeInstructions.text = "요리법: ${recipe.instructions}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
