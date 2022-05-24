package com.example.testapplication.view.fragment

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.testapplication.databinding.FragmentFavoriteCharactersBinding
import com.example.testapplication.view.adapter.CharacterListAdapter
import com.example.testapplication.view.adapter.FavoriteCharactersAdapter
import com.example.testapplication.view.fragment.BaseFragment
import com.example.testapplication.vm.CharactersListViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavoriteCharactersFragment : BaseFragment<FragmentFavoriteCharactersBinding>() {
    private val charactersListViewModel: CharactersListViewModel by activityViewModels()
    private lateinit var favoriteCharactersAdapter: FavoriteCharactersAdapter

    override fun getViewBinding() = FragmentFavoriteCharactersBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupObservers()
    }

    private fun setupUi() {
        favoriteCharactersAdapter = FavoriteCharactersAdapter { character, card ->
            charactersListViewModel.deleteCharactersFromFavorites(listOf(character))
        }
        with(binding) {
            characterRecyclerview.apply {
                setHasFixedSize(true)
                val layoutManager = GridLayoutManager(context, 5)
                if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    layoutManager.spanCount = 3
                }
                characterRecyclerview.adapter = favoriteCharactersAdapter
                characterRecyclerview.layoutManager = layoutManager
            }
        }
    }


    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.apply {
            launch {
                charactersListViewModel.favoriteCharacters.observe(viewLifecycleOwner) {
                    favoriteCharactersAdapter.submitList(it)
                }
            }
        }
    }
}

