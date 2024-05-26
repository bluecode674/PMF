package com.example.pmf.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pmf.R
import com.example.pmf.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 클릭 가능한 텍스트뷰에 대한 클릭 리스너 설정
        binding.textViewClickable.setOnClickListener {
            navigateToNotificationSettings()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToNotificationSettings() {
        // 알림 설정 화면으로 이동하는 네비게이션 액션 실행
        findNavController().navigate(R.id.notificationSettingFragment)
    }
}
