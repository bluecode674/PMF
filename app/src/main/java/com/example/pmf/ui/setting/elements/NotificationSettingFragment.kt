package com.example.pmf.ui.setting.elements

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
        inflater: LayoutInflater,
        container: ViewGroup?,
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

        // 저장 로직 구현
        // 여기서는 예시로 설정을 저장한 후 설정 페이지로 이동하는 것으로 대체합니다.
        saveSettings(daysBefore)
    }

    private fun saveSettings(daysBefore: Int) {
        // 설정을 저장하는 로직을 여기에 추가
        // 예를 들어, SharedPreferences를 사용하여 설정을 저장할 수 있습니다.

        // 저장 후 설정 페이지로 이동
        findNavController().navigate(R.id.navigation_setting)

    }
}
