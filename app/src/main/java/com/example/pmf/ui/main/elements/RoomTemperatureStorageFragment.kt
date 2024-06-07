package com.example.pmf.ui.main.elements

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pmf.DB.DBHelper
import com.example.pmf.DB.Ingredient
import com.example.pmf.R
import java.text.SimpleDateFormat
import java.util.*

class RoomTemperatureStorageFragment : Fragment() {

    private lateinit var dbHelper: DBHelper
    private lateinit var roomTempRecyclerView: RecyclerView
    private lateinit var ingredientAdapter: IngredientAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_roomtemperaturestorage, container, false)
        dbHelper = DBHelper(requireContext())
        roomTempRecyclerView = view.findViewById(R.id.roomTempRecyclerView)
        roomTempRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val roomTempStorageIngredients = dbHelper.searchItemsByStorageLocation("실온")
        ingredientAdapter = IngredientAdapter(roomTempStorageIngredients) { ingredient ->
            showEditDeleteDialog(ingredient)
        }
        roomTempRecyclerView.adapter = ingredientAdapter

        return view
    }

    fun filterItems(query: String) {
        val filteredList = dbHelper.searchItemsByStorageLocation("실온").filter {
            it.name.contains(query, ignoreCase = true)
        }
        ingredientAdapter.updateList(filteredList)
    }

    private fun showEditDeleteDialog(ingredient: Ingredient) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_delete, null)
        val tvName = dialogView.findViewById<TextView>(R.id.tv_name)
        val tvPurchaseDate = dialogView.findViewById<TextView>(R.id.tv_purchase_date)
        val etExpiryDate = dialogView.findViewById<EditText>(R.id.et_expiry_date)
        val etQuantity = dialogView.findViewById<EditText>(R.id.et_quantity)

        tvName.text = "재료 이름: ${ingredient.name}"
        tvPurchaseDate.text = "구매일: ${ingredient.purchaseDate}"
        etExpiryDate.setText(ingredient.expiryDate)
        etQuantity.setText(ingredient.quantity.toString())

        etExpiryDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(requireContext(), { _, year, month, day ->
                calendar.set(year, month, day)
                val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                etExpiryDate.setText(dateFormatter.format(calendar.time))
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("재료 수정 또는 삭제")
            .setView(dialogView)
            .setPositiveButton("수정") { _, _ ->
                val updatedExpiryDate = etExpiryDate.text.toString()
                val updatedQuantity = etQuantity.text.toString().toInt()

                dbHelper.updateItem(
                    ingredient.name,
                    ingredient.purchaseDate,
                    updatedExpiryDate,
                    updatedQuantity
                )

                // 목록 갱신
                filterItems("")
            }
            .setNegativeButton("삭제") { _, _ ->
                dbHelper.deleteItem(ingredient.name, ingredient.purchaseDate)

                // 목록 갱신
                filterItems("")
            }
            .setNeutralButton("취소", null)
            .create()

        dialog.show()
    }
}
