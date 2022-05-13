package com.example.testapplication.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.cardview.widget.CardView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.testapplication.R
import com.example.testapplication.databinding.FragmentCharactersListBinding
import com.example.testapplication.domain.model.Character
import com.example.testapplication.domain.model.CharacterStatus
import com.example.testapplication.domain.model.FilterCharacters
import com.example.testapplication.util.PagerEvents
import com.example.testapplication.util.PagingLoadStateAdapter
import com.example.testapplication.view.adapter.CharacterListAdapter
import com.example.testapplication.vm.CharactersListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CharactersListFragment : Fragment(), FilterDialogFragment.NoticeDialogListener {
    private val charactersListViewModel: CharactersListViewModel by activityViewModels()
    private var _binding: FragmentCharactersListBinding? = null

    private lateinit var characterListAdapter: CharacterListAdapter

    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentCharactersListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filter -> {
                val dialogFragment = FilterDialogFragment(
                    previousFilters = charactersListViewModel.filterCharactersFlow.value
                )
                dialogFragment.show(childFragmentManager, "filter")
                dialogFragment.setCallback(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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

    override fun onDialogPositiveClick(filter: FilterCharacters) {
        charactersListViewModel.onFiltersChange(filter)
    }

    private fun characterItemClicked(character: Character, cardView: CardView) {
        val extras = FragmentNavigatorExtras(
            cardView to "${character.id}-${character.image}"
        )
        val action = CharactersListFragmentDirections
            .actionCharacterListFragmentToCharacterFragment(
                id = character.id,
                uri = character.image,
                name = character.name,
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
        characterListAdapter = CharacterListAdapter(::characterItemClicked)

        with(binding) {
            characterRecyclerview.apply {
                layoutManager = GridLayoutManager(context, 2)
                setHasFixedSize(true)
                adapter = characterListAdapter.withLoadStateHeaderAndFooter(
                    header = PagingLoadStateAdapter(characterListAdapter),
                    footer = PagingLoadStateAdapter(characterListAdapter)
                )
            }

            swipeRefreshLayout.setOnRefreshListener {
                characterListAdapter.refresh()
            }
        }
    }


    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.apply {
            // Controls refresh indicator
            launch {
                characterListAdapter.loadStateFlow.collectLatest {
                    binding.swipeRefreshLayout.isRefreshing = it.refresh is LoadState.Loading
                    if (it.refresh is LoadState.Error && characterListAdapter.itemCount == 0) {
                        binding.errorMessage.visibility = View.VISIBLE
                    } else {
                        binding.errorMessage.visibility = View.GONE
                    }
                }
            }

            // Submits data to recyclerView
            launch {
                charactersListViewModel.charactersFlow.collectLatest { pagingData ->
                    characterListAdapter.submitData(pagingData)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}