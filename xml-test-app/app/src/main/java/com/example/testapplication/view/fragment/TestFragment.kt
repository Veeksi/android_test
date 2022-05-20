package com.example.testapplication.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.testapplication.R
import com.example.testapplication.databinding.FragmentTestBinding

class TestFragment : BaseFragment<FragmentTestBinding>() {

    override fun getViewBinding() = FragmentTestBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationIcon(R.drawable.ic_back)
        setHasOptionsMenu(true)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}