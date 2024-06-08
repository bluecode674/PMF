package com.example.pmf.ui.ingredient

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.pmf.DB.DBHelper
import com.example.pmf.R
import java.text.SimpleDateFormat
import java.util.*

class IngredientFragment : Fragment() {

    private lateinit var dbHelper: DBHelper
    private lateinit var selectedPurchaseDate: String
    private lateinit var selectedExpiryDate: String
    private lateinit var selectedIngredient: String
    private lateinit var btnExpiryDate: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_ingredient, container, false)

        dbHelper = DBHelper(requireContext())

        val btnSelectIngredient: Button = root.findViewById(R.id.btn_select_ingredient)
        val btnAdd: Button = root.findViewById(R.id.btn_add)
        val btnPurchaseDate: Button = root.findViewById(R.id.btn_purchase_date)
        btnExpiryDate = root.findViewById(R.id.btn_expiry_date)
        val etQuantity: EditText = root.findViewById(R.id.et_quantity)
        val spinnerStorageLocation: Spinner = root.findViewById(R.id.spinner_storage_location)

        btnSelectIngredient.setOnClickListener {
            findNavController().navigate(R.id.action_ingredientFragment_to_ingredientSelectionFragment)
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
                btnExpiryDate.isEnabled = true // 소비기한 버튼 활성화
                btnPurchaseDate.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue)) // 색상 변경
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

            // 구매일은 오늘 이전 날짜만 선택 가능
            datePicker.datePicker.maxDate = System.currentTimeMillis()
            datePicker.show()
        }

        btnExpiryDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(requireContext(), { _, year, month, day ->
                calendar.set(year, month, day)
                selectedExpiryDate = dateFormatter.format(calendar.time)
                btnExpiryDate.text = selectedExpiryDate
                btnExpiryDate.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue)) // 색상 변경
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

            // 소비기한은 구매일 이후 날짜만 선택 가능
            val purchaseDate = dateFormatter.parse(selectedPurchaseDate)?.time
            if (purchaseDate != null) {
                datePicker.datePicker.minDate = purchaseDate + 86400000 // 하루 후
            }
            datePicker.show()
        }

        btnAdd.setOnClickListener {
            val storageLocation = spinnerStorageLocation.selectedItem.toString()
            val quantityStr = etQuantity.text.toString().trim()

            // 수량은 음수나 0이 들어올 수 없게 처리
            val quantity = quantityStr.toIntOrNull()
            if (quantity == null || quantity <= 0) {
                Toast.makeText(requireContext(), "올바른 수량을 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (::selectedIngredient.isInitialized && ::selectedPurchaseDate.isInitialized && ::selectedExpiryDate.isInitialized) {
                dbHelper.addItem(selectedIngredient, selectedPurchaseDate, selectedExpiryDate, storageLocation, quantity)
                Toast.makeText(requireContext(), "재료가 추가되었습니다.", Toast.LENGTH_SHORT).show()

                // 필드 초기화
                btnSelectIngredient.text = "재료 선택"
                btnPurchaseDate.text = "구매 날짜 선택"
                btnExpiryDate.text = "소비 기한 선택"
                spinnerStorageLocation.setSelection(0)
                etQuantity.text.clear()

                // 초기화 변수
                selectedIngredient = ""
                selectedPurchaseDate = ""
                selectedExpiryDate = ""
                btnExpiryDate.isEnabled = false // 소비기한 버튼 비활성화
            } else {
                Toast.makeText(requireContext(), "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // FragmentResultListener를 통해 데이터 수신 설정
        setFragmentResultListener("ingredientSelectionKey") { _, bundle ->
            val result = bundle.getString("selectedIngredient")
            if (result != null) {
                selectedIngredient = result
                btnSelectIngredient.text = selectedIngredient
                btnSelectIngredient.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue)) // 색상 변경
            }
        }

        btnExpiryDate.isEnabled = false // 초기에는 소비기한 버튼 비활성화

        return root
    }
}
