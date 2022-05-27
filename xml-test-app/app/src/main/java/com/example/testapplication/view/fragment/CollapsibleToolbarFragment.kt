package com.example.testapplication.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.testapplication.R
import com.example.testapplication.databinding.FragmentCollapsibleToolbarBinding

class CollapsibleToolbarFragment : BaseFragment<FragmentCollapsibleToolbarBinding>() {

    override fun getViewBinding() = FragmentCollapsibleToolbarBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setToolbarBackButton(binding.toolbar)
    }
}