package com.example.pmf.ui.setting.elements

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pmf.R

class NotificationSettingFragment : Fragment() {

    private lateinit var notificationDateEditText: EditText
    private lateinit var saveButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_notification_setting, container, false)

        notificationDateEditText = root.findViewById(R.id.editTextNotificationDate)
        saveButton = root.findViewById(R.id.buttonSave)

        saveButton.setOnClickListener {
            saveNotificationDate()
        }

        return root
    }

    private fun saveNotificationDate() {
        val notificationDateText = notificationDateEditText.text.toString().trim()

        if (notificationDateText.isEmpty()) {
            Toast.makeText(requireContext(), "알림 날짜를 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val daysBefore = notificationDateText.toIntOrNull()
        if (daysBefore == null || daysBefore <= 0) {
            Toast.makeText(requireContext(), "올바른 알림 날짜를 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        // SharedPreferences에 저장
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putInt("notification_days_before", daysBefore)
            apply()
        }

        Toast.makeText(requireContext(), "알림 설정이 저장되었습니다.", Toast.LENGTH_SHORT).show()

        // 설정 후 설정 페이지로 이동
        findNavController().navigate(R.id.navigation_setting)
    }
}
