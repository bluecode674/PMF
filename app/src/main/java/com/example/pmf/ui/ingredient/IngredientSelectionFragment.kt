package com.example.pmf.ui.ingredient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.pmf.DB.BasicIngredientsDBHelper
import com.example.pmf.R
import java.util.Locale

class IngredientSelectionFragment : Fragment() {

    private lateinit var basicIngredientsDBHelper: BasicIngredientsDBHelper
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var basicIngredients: Array<String>
    private lateinit var filteredIngredients: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_ingredient_selection, container, false)

        basicIngredientsDBHelper = BasicIngredientsDBHelper(requireContext())
        basicIngredients = basicIngredientsDBHelper.getAllItems().map { it.name }.toTypedArray()
        filteredIngredients = ArrayList(basicIngredients.toList())

        val listView: ListView = root.findViewById(R.id.list_view_ingredients)
        val searchView: SearchView = root.findViewById(R.id.search_view)

        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, filteredIngredients)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedIngredient = filteredIngredients[position]
            val result = Bundle().apply { putString("selectedIngredient", selectedIngredient) }
            setFragmentResult("ingredientSelectionKey", result)
            findNavController().popBackStack()
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filteredIngredients.clear()
                if (!newText.isNullOrEmpty()) {
                    val search = newText.lowercase(Locale.getDefault())
                    basicIngredients.forEach { ingredient ->
                        if (ingredient.lowercase(Locale.getDefault()).contains(search)) {
                            filteredIngredients.add(ingredient)
                        }
                    }
                } else {
                    filteredIngredients.addAll(basicIngredients)
                }
                adapter.notifyDataSetChanged()
                return true
            }
        })

        return root
    }
}
