package com.example.testapplication.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testapplication.R
import com.example.testapplication.databinding.FragmentOtherBinding
import com.example.testapplication.domain.model.NavigationItem
import com.example.testapplication.domain.model.navigationItems
import com.example.testapplication.view.adapter.PageAdapter

class OtherFragment : BaseFragment<FragmentOtherBinding>() {
    override fun getViewBinding() = FragmentOtherBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pageAdapter = PageAdapter(::onNavigationItemCLicked)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = pageAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        pageAdapter.submitList(navigationItems)
    }

    private fun onNavigationItemCLicked(navigationItem: NavigationItem) {
        try {
            view?.findNavController()?.navigate(navigationItem.action)
        } catch (e: Throwable) {
        }
    }
}