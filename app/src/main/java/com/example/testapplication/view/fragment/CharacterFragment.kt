package com.example.testapplication.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testapplication.databinding.FragmentCharactersBinding
import com.example.testapplication.domain.model.Character
import com.example.testapplication.util.PagerEvents
import com.example.testapplication.util.PagingLoadStateAdapter
import com.example.testapplication.view.adapter.CharacterAdapter
import com.example.testapplication.viewModel.CharacterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CharacterFragment : Fragment() {
    private val characterViewModel: CharacterViewModel by viewModels()
    private var _binding: FragmentCharactersBinding? = null
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
        _binding = FragmentCharactersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupObservers()
    }

    fun characterItemClicked(character: Character) {
        val modalBottomSheet = ModalBottomSheet(
            onLike = { characterViewModel.onViewEvent(PagerEvents.Like(character)) },
            onDelete = { characterViewModel.onViewEvent(PagerEvents.Remove(character)) }
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
                characterViewModel.charactersFlow.collectLatest { pagingData ->
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