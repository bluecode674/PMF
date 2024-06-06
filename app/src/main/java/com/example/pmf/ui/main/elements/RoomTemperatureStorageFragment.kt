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

class RoomTemperatureStorageFragment : Fragment() {

    private lateinit var dbHelper: DBHelper
    private lateinit var roomTempRecyclerView: RecyclerView
    private lateinit var ingredientAdapter: IngredientAdapter
    private lateinit var roomTempStorageIngredients: List<Ingredient>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_roomtemperaturestorage, container, false)
        dbHelper = DBHelper(requireContext())
        roomTempRecyclerView = view.findViewById(R.id.roomTempRecyclerView)
        roomTempRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        roomTempStorageIngredients = dbHelper.searchItemsByStorageLocation("실온")
        ingredientAdapter = IngredientAdapter(roomTempStorageIngredients)
        roomTempRecyclerView.adapter = ingredientAdapter

        return view
    }

    fun filterItems(query: String?) {
        val filteredList = if (query.isNullOrEmpty()) {
            roomTempStorageIngredients
        } else {
            roomTempStorageIngredients.filter {
                it.name.contains(query, ignoreCase = true) || it.storageLocation.contains(query, ignoreCase = true)
            }
        }
        ingredientAdapter.updateList(filteredList)
    }
}
