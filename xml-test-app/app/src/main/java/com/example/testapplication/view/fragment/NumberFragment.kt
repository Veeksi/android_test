package com.example.testapplication.view.fragment

import android.os.Bundle
import android.view.View
import com.example.testapplication.databinding.FragmentNumberBinding

class NumberFragment : BaseFragment<FragmentNumberBinding>() {
    override fun getViewBinding() = FragmentNumberBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            binding.textNumber.text = getInt(ARG_OBJECT).toString()
        }
    }

    companion object {
        const val ARG_OBJECT = "object"
    }
}