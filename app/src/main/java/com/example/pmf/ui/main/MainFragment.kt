package com.example.pmf.ui.main

import com.example.pmf.ui.main.elements.ColdStorageFragment
import com.example.pmf.ui.main.elements.FreezeFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.pmf.R
import com.example.pmf.databinding.FragmentMainBinding
import com.example.pmf.ui.main.elements.RoomTemperatureStorageFragment

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 초기 프래그먼트 설정
        childFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, ColdStorageFragment())
            commitNow()
        }

        // 각 버튼에 클릭 리스너 설정
        binding.buttonFragment1.setOnClickListener { setFragment(ColdStorageFragment()) }
        binding.buttonFragment2.setOnClickListener { setFragment(FreezeFragment()) }
        binding.buttonFragment3.setOnClickListener { setFragment(RoomTemperatureStorageFragment()) }
    }

    private fun setFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, fragment)
            commitNow()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}