package com.example.pmf.ui.recipe

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pmf.DB.RecipeDBHelper
import com.example.pmf.R
import com.example.pmf.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var recipeDBHelper: RecipeDBHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipeDBHelper = RecipeDBHelper(requireContext())

        recipeAdapter = RecipeAdapter(emptyList()) { recipeId ->
            val bundle = Bundle().apply {
                putInt("recipeId", recipeId)
            }
            findNavController().navigate(R.id.action_recipeFragment_to_recipeDetailFragment, bundle)
        }
        binding.recyclerViewRecipes.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewRecipes.adapter = recipeAdapter

        // 초기 레시피 목록 표시
        updateRecipeList()

        // 검색 필터링
        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateRecipeList(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun updateRecipeList(query: String = "") {
        val recipes = if (query.isEmpty()) {
            recipeDBHelper.getAllRecipes()
        } else {
            recipeDBHelper.searchRecipes(query)
        }
        recipeAdapter.updateRecipes(recipes)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
