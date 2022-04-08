package com.example.testapplication.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testapplication.R
import com.example.testapplication.databinding.FragmentCharactersListBinding
import com.example.testapplication.domain.model.Character
import com.example.testapplication.util.PagerEvents
import com.example.testapplication.util.PagingLoadStateAdapter
import com.example.testapplication.view.adapter.CharacterAdapter
import com.example.testapplication.vm.CharactersListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CharactersListFragment : Fragment() {
    private val charactersListViewModel: CharactersListViewModel by viewModels()
    private var _binding: FragmentCharactersListBinding? = null

    private lateinit var characterAdapter: CharacterAdapter

    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharactersListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        setupUi()
        setupObservers()
        // Postpones return transition when the view is ready
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }

    }

    private fun characterItemClicked(character: Character, imageView: ImageView) {
        val extras = FragmentNavigatorExtras(
            imageView to "${character.id}-${character.image}"
        )
        val action = CharactersListFragmentDirections
            .actionCharacterListFragmentToCharacterFragment(
                id = character.id,
                uri = character.image
            )

        val modalBottomSheet = ModalBottomSheet(
            onView = {
                findNavController().navigate(
                    action,
                    extras,
                )
            },
            onLike = { charactersListViewModel.onViewEvent(PagerEvents.Like(character)) },
            onDelete = { charactersListViewModel.onViewEvent(PagerEvents.Remove(character)) }
        )

        // Ensures that no multiple dialogs can be visible
        if (childFragmentManager.findFragmentByTag("dialogTAG") == null) {
            modalBottomSheet.show(childFragmentManager, "dialogTAG")
        }
    }

    private fun setupUi() {
        characterAdapter = CharacterAdapter(this, ::characterItemClicked)

        with(binding) {
            characterRecyclerview.apply {
                layoutManager = GridLayoutManager(context, 3)
                setHasFixedSize(true)
                adapter = characterAdapter.withLoadStateHeaderAndFooter(
                    header = PagingLoadStateAdapter(characterAdapter),
                    footer = PagingLoadStateAdapter(characterAdapter)
                )
            }

            swipeRefreshLayout.setOnRefreshListener {
                characterAdapter.refresh()
            }
        }
    }


    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.apply {
            // Controls refresh indicator
            launch {
                characterAdapter.loadStateFlow.collectLatest {
                    binding.swipeRefreshLayout.isRefreshing = it.refresh is LoadState.Loading
                }
            }

            // Submits data to recyclerView
            launch {
                charactersListViewModel.charactersFlow.collectLatest { pagingData ->
                    characterAdapter.submitData(pagingData)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}