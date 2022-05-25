package com.example.testapplication.view.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.testapplication.R
import com.example.testapplication.databinding.FragmentFavoriteCharactersBinding
import com.example.testapplication.domain.model.Character
import com.example.testapplication.view.adapter.CharacterListAdapter
import com.example.testapplication.view.adapter.FavoriteCharactersAdapter
import com.example.testapplication.view.fragment.BaseFragment
import com.example.testapplication.vm.CharactersListViewModel
import com.example.testapplication.vm.FavoriteCharactersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteCharactersFragment : BaseFragment<FragmentFavoriteCharactersBinding>() {
    private val favoriteCharactersViewModel: FavoriteCharactersViewModel by viewModels()
    private lateinit var favoriteCharactersAdapter: FavoriteCharactersAdapter

    override fun getViewBinding() = FragmentFavoriteCharactersBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupObservers()
    }

    private fun setupUi() {
        favoriteCharactersAdapter = FavoriteCharactersAdapter(::onDeleteCharacter)
        with(binding) {
            characterRecyclerview.apply {
                val layoutManager = GridLayoutManager(context, 5)
                if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    layoutManager.spanCount = 3
                }
                characterRecyclerview.adapter = favoriteCharactersAdapter
                characterRecyclerview.layoutManager = layoutManager
                setHasFixedSize(true)
            }
        }
    }


    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.apply {
            launch {
                favoriteCharactersViewModel.favoriteCharacters.observe(viewLifecycleOwner) {
                    favoriteCharactersAdapter.submitList(it)
                }
            }
        }
    }

    private fun onDeleteCharacter(character: Character) {
        favoriteCharactersViewModel.deleteCharactersFromFavorites(listOf(character))
        notifyWithAction(R.string.item_deleted, R.string.undo) {
            favoriteCharactersViewModel.addCharactersToFavorites(listOf(character))
        }
    }
}

