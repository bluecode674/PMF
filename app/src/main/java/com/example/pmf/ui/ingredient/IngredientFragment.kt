package com.example.pmf.ui.ingredient

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.pmf.DB.DBHelper
import com.example.pmf.DB.BasicIngredientsDBHelper
import com.example.pmf.R
import java.text.SimpleDateFormat
import java.util.*

class IngredientFragment : Fragment() {

    private lateinit var dbHelper: DBHelper
    private lateinit var selectedPurchaseDate: String
    private lateinit var selectedExpiryDate: String
    private lateinit var selectedIngredient: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_ingredient, container, false)

        dbHelper = DBHelper(requireContext())

        // 기본 재료 목록을 불러와 팝업 다이얼로그에 설정
        val basicIngredientsDBHelper = BasicIngredientsDBHelper(requireContext())
        val basicIngredients = basicIngredientsDBHelper.getAllItems().map { it.name }.toTypedArray()

        val btnSelectIngredient: Button = root.findViewById(R.id.btn_select_ingredient)
        val btnAdd: Button = root.findViewById(R.id.btn_add)
        val btnPurchaseDate: Button = root.findViewById(R.id.btn_purchase_date)
        val btnExpiryDate: Button = root.findViewById(R.id.btn_expiry_date)
        val spinnerStorageLocation: Spinner = root.findViewById(R.id.spinner_storage_location)

        btnSelectIngredient.setOnClickListener {
            showIngredientSelectionDialog(basicIngredients)
        }

        // 보관 장소 스피너 설정
        val storageLocations = arrayOf("냉장고", "냉동고", "실온")
        val storageAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, storageLocations)
        storageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStorageLocation.adapter = storageAdapter

        // 날짜 선택 설정
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        btnPurchaseDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(requireContext(), { _, year, month, day ->
                calendar.set(year, month, day)
                selectedPurchaseDate = dateFormatter.format(calendar.time)
                btnPurchaseDate.text = selectedPurchaseDate
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }

        btnExpiryDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(requireContext(), { _, year, month, day ->
                calendar.set(year, month, day)
                selectedExpiryDate = dateFormatter.format(calendar.time)
                btnExpiryDate.text = selectedExpiryDate
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }

        btnAdd.setOnClickListener {
            val storageLocation = spinnerStorageLocation.selectedItem.toString()

            if (::selectedIngredient.isInitialized && ::selectedPurchaseDate.isInitialized && ::selectedExpiryDate.isInitialized) {
                dbHelper.addItem(selectedIngredient, selectedPurchaseDate, selectedExpiryDate, storageLocation)
            } else {
                // Handle error (e.g., show a toast to the user)
            }
        }

        return root
    }

    private fun showIngredientSelectionDialog(ingredients: Array<String>) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("재료 선택")
        builder.setItems(ingredients) { dialog, which ->
            selectedIngredient = ingredients[which]
            view?.findViewById<Button>(R.id.btn_select_ingredient)?.text = selectedIngredient
        }
        builder.show()
    }
}
