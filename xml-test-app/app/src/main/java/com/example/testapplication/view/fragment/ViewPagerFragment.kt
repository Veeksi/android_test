package com.example.testapplication.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.testapplication.view.fragment.NumberFragment.Companion.ARG_OBJECT
import com.example.testapplication.databinding.FragmentViewPagerBinding
import com.google.android.material.tabs.TabLayoutMediator

class ViewPagerFragment : BaseFragment<FragmentViewPagerBinding>() {
    private lateinit var demoCollectionAdapter: DemoCollectionAdapter
    private lateinit var viewPager2: ViewPager2

    override fun getViewBinding() = FragmentViewPagerBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setToolbarBackButton(binding.toolbar)
        demoCollectionAdapter = DemoCollectionAdapter(this)
        binding.apply {
            viewPager.adapter = demoCollectionAdapter
            TabLayoutMediator(binding.tabLayout, viewPager) { tab, position ->
                tab.text = "Tab ${position + 1}"
            }.attach()
        }
    }
}

class DemoCollectionAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 50

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        val fragment = NumberFragment()
        fragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putInt(ARG_OBJECT, position + 1)
        }
        return fragment
    }
}