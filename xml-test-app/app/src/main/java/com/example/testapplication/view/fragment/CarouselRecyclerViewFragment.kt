package com.example.testapplication.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.HORIZONTAL
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.example.testapplication.R
import com.example.testapplication.databinding.FragmentCarouselRecyclerViewBinding
import com.example.testapplication.domain.model.Character
import com.example.testapplication.util.navigate
import com.example.testapplication.view.adapter.CharacterListAdapter
import com.example.testapplication.view.fragment.BaseFragment
import com.example.testapplication.vm.CharactersListViewModel
import com.google.android.material.card.MaterialCardView
import com.takusemba.multisnaprecyclerview.MultiSnapHelper
import com.takusemba.multisnaprecyclerview.SnapGravity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CarouselRecyclerViewFragment : BaseFragment<FragmentCarouselRecyclerViewBinding>() {
    private val charactersListViewModel: CharactersListViewModel by activityViewModels()
    private lateinit var characterListAdapter: CharacterListAdapter

    override fun getViewBinding() = FragmentCarouselRecyclerViewBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbarBackButton(binding.toolbar)
        setupUi()
        setupObservers()
    }

    private fun setupUi() {
        val multiSnapHelper = MultiSnapHelper(SnapGravity.START, 1, 100f)
        multiSnapHelper.attachToRecyclerView(binding.recyclerView)
        characterListAdapter = CharacterListAdapter(::characterItemClicked)
        binding.recyclerView.apply {
            setHasFixedSize(true)
            this.adapter = characterListAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.apply {

            launch {
                charactersListViewModel.charactersFlow.collectLatest { pagingData ->
                    characterListAdapter.submitData(pagingData)
                }
            }
        }
    }

    private fun characterItemClicked(
        character: Character,
        card: MaterialCardView,
    ) {

    }
}