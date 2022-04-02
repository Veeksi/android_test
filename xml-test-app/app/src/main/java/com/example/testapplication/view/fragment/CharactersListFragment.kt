package com.example.testapplication.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testapplication.R
import com.example.testapplication.databinding.FragmentCharactersListBinding
import com.example.testapplication.domain.model.Character
import com.example.testapplication.util.PagerEvents
import com.example.testapplication.util.PagingLoadStateAdapter
import com.example.testapplication.view.adapter.CharacterAdapter
import com.example.testapplication.viewModel.CharactersListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CharactersListFragment : Fragment() {
    private val charactersListViewModel: CharactersListViewModel by viewModels()
    private var _binding: FragmentCharactersListBinding? = null

    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    lateinit var characterAdapter: CharacterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
        setupUi()
        setupObservers()
    }

    private fun characterItemClicked(character: Character) {
        val bundle = Bundle()
        bundle.putInt("id", character.id)

        val modalBottomSheet = ModalBottomSheet(
            onView = { findNavController().navigate(R.id.action_characterListFragment_to_characterFragment, bundle) },
            onLike = { charactersListViewModel.onViewEvent(PagerEvents.Like(character)) },
            onDelete = { charactersListViewModel.onViewEvent(PagerEvents.Remove(character)) }
        )
        modalBottomSheet.show(childFragmentManager, "TAG")
    }

    private fun setupUi() {
        characterAdapter = CharacterAdapter { character ->
            characterItemClicked(character)
        }
        with(binding) {
            characterRecyclerview.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = characterAdapter.withLoadStateHeaderAndFooter(
                    header = PagingLoadStateAdapter(characterAdapter),
                    footer = PagingLoadStateAdapter(characterAdapter)
                )
                addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
            }

            swipeRefreshLayout.setOnRefreshListener {
                characterAdapter.refresh()
            }
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.apply {
            // Submits data to recyclerView
            launchWhenCreated {
                charactersListViewModel.charactersFlow.collectLatest { pagingData ->
                    characterAdapter.submitData(pagingData)
                }
            }

            // Controls refresh indicator
            launchWhenCreated {
                characterAdapter.loadStateFlow.collectLatest {
                    binding.swipeRefreshLayout.isRefreshing = it.refresh is LoadState.Loading
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}