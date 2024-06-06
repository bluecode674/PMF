package com.example.pmf.ui.main.elements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pmf.DB.DBHelper
import com.example.pmf.DB.Ingredient
import com.example.pmf.R

class ColdStorageFragment : Fragment() {

    private lateinit var dbHelper: DBHelper
    private lateinit var refrigeratorRecyclerView: RecyclerView
    private lateinit var ingredientAdapter: IngredientAdapter
    private lateinit var coldStorageIngredients: List<Ingredient>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_coldstorage, container, false)
        dbHelper = DBHelper(requireContext())
        refrigeratorRecyclerView = view.findViewById(R.id.refrigeratorRecyclerView)
        refrigeratorRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        coldStorageIngredients = dbHelper.searchItemsByStorageLocation("냉장고")
        ingredientAdapter = IngredientAdapter(coldStorageIngredients)
        refrigeratorRecyclerView.adapter = ingredientAdapter

        return view
    }

    fun filterItems(query: String?) {
        val filteredList = if (query.isNullOrEmpty()) {
            coldStorageIngredients
        } else {
            coldStorageIngredients.filter {
                it.name.contains(query, ignoreCase = true) || it.storageLocation.contains(query, ignoreCase = true)
            }
        }
        ingredientAdapter.updateList(filteredList)
    }
}
