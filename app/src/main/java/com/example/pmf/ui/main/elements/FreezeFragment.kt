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

class FreezeFragment : Fragment() {

    private lateinit var dbHelper: DBHelper
    private lateinit var freezeRecyclerView: RecyclerView
    private lateinit var ingredientAdapter: IngredientAdapter
    private lateinit var freezeStorageIngredients: List<Ingredient>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_freeze, container, false)
        dbHelper = DBHelper(requireContext())
        freezeRecyclerView = view.findViewById(R.id.freezeRecyclerView)
        freezeRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        freezeStorageIngredients = dbHelper.searchItemsByStorageLocation("냉동고")
        ingredientAdapter = IngredientAdapter(freezeStorageIngredients)
        freezeRecyclerView.adapter = ingredientAdapter

        return view
    }

    fun filterItems(query: String?) {
        val filteredList = if (query.isNullOrEmpty()) {
            freezeStorageIngredients
        } else {
            freezeStorageIngredients.filter {
                it.name.contains(query, ignoreCase = true) || it.storageLocation.contains(query, ignoreCase = true)
            }
        }
        ingredientAdapter.updateList(filteredList)
    }
}
