package com.example.pmf.ui.main.elements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pmf.DBHelper
import com.example.pmf.R
import com.example.pmf.Ingredient

class RoomTemperatureStorageFragment : Fragment() {

    private lateinit var dbHelper: DBHelper
    private lateinit var roomTemperatureRecyclerView: RecyclerView
    private lateinit var ingredientAdapter: IngredientAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_roomtemperaturestorage, container, false)
        dbHelper = DBHelper(requireContext())
        roomTemperatureRecyclerView = view.findViewById(R.id.roomTemperatureRecyclerView)
        roomTemperatureRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val roomTemperatureStorageIngredients = dbHelper.searchItemsByStorageLocation("실온")
        ingredientAdapter = IngredientAdapter(roomTemperatureStorageIngredients)
        roomTemperatureRecyclerView.adapter = ingredientAdapter

        return view
    }
}
