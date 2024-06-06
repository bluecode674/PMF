package com.example.pmf.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pmf.R
import com.example.pmf.databinding.FragmentMainBinding
import com.example.pmf.ui.main.elements.ColdStorageFragment
import com.example.pmf.ui.main.elements.FreezeFragment
import com.example.pmf.ui.main.elements.RoomTemperatureStorageFragment

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private var currentFragment: Fragment? = null

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
        setFragment(ColdStorageFragment())

        // 각 버튼에 클릭 리스너 설정
        binding.buttonFragment1.setOnClickListener { setFragment(ColdStorageFragment()) }
        binding.buttonFragment2.setOnClickListener { setFragment(FreezeFragment()) }
        binding.buttonFragment3.setOnClickListener { setFragment(RoomTemperatureStorageFragment()) }

        // 검색 필터링
        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterCurrentFragment(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setFragment(fragment: Fragment) {
        currentFragment = fragment
        childFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, fragment)
            commitNow()
        }
    }

    private fun filterCurrentFragment(query: String) {
        when (currentFragment) {
            is ColdStorageFragment -> (currentFragment as ColdStorageFragment).filterItems(query)
            is FreezeFragment -> (currentFragment as FreezeFragment).filterItems(query)
            is RoomTemperatureStorageFragment -> (currentFragment as RoomTemperatureStorageFragment).filterItems(query)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
