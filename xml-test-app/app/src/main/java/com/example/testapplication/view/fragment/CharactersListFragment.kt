package com.example.testapplication.view.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.R
import com.example.testapplication.databinding.FragmentCharactersListBinding
import com.example.testapplication.domain.model.Character
import com.example.testapplication.domain.model.FilterCharacters
import com.example.testapplication.util.PagerEvents
import com.example.testapplication.util.PagingLoadStateAdapter
import com.example.testapplication.view.adapter.CharacterListAdapter
import com.example.testapplication.vm.CharactersListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException


@AndroidEntryPoint
class CharactersListFragment : Fragment() {
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
                    previousFilters = charactersListViewModel.filterCharactersFlow.value,
                    onSubmitFilter = {
                        onSubmitFilter(it)
                    }
                )
                dialogFragment.show(childFragmentManager, "filter")
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
            onView = { findNavController().navigate(action, extras) },
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

                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        // Shows FAB if the recyclerView can be scrolled upwards
                        if (recyclerView.canScrollVertically(-1)) {
                            floatingActionButton.visibility = View.VISIBLE
                        } else {
                            floatingActionButton.visibility = View.GONE
                        }
                    }
                })
            }

            swipeRefreshLayout.setOnRefreshListener {
                characterListAdapter.refresh()
            }

            floatingActionButton.setOnClickListener {
                scrollToTop()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.apply {
            if (characterRecyclerview.canScrollVertically(-1)) {
                floatingActionButton.visibility = View.VISIBLE
            }
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.apply {
            launch {
                characterListAdapter.loadStateFlow.collectLatest { loadState ->
                    binding.swipeRefreshLayout.isRefreshing = false
                    when (loadState.refresh) {
                        is LoadState.Error -> {
                            showErrorToast(loadState)
                            binding.errorMessage.visibility = View.VISIBLE
                            binding.circularProgressIndicator.visibility = View.GONE
                            binding.characterRecyclerview.visibility = View.GONE
                        }
                        is LoadState.Loading -> {
                            if (characterListAdapter.itemCount == 0) {
                                binding.errorMessage.visibility = View.GONE
                                binding.circularProgressIndicator.visibility = View.VISIBLE
                            }
                        }
                        is LoadState.NotLoading -> {
                            binding.errorMessage.visibility = View.GONE
                            binding.circularProgressIndicator.visibility = View.GONE
                            binding.characterRecyclerview.visibility = View.VISIBLE
                        }
                    }
                }
            }

            launch {
                charactersListViewModel.charactersFlow.collectLatest { pagingData ->
                    characterListAdapter.submitData(pagingData)
                }
            }
        }
    }

    private fun showErrorToast(loadState: CombinedLoadStates) {
        val errorState = when {
            loadState.append is LoadState.Error -> loadState.append as LoadState.Error
            loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
            loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
            else -> null
        }
        errorState?.let {
            var errorMessage = getString(R.string.unknown_error)
            if (it.error is UnknownHostException) {
                errorMessage = getString(R.string.internet_error)
            } else if (it.error is HttpException) {
                errorMessage = getString(R.string.not_found_error)
            }
            binding.errorMessage.text = errorMessage
            Toast.makeText(
                requireContext(),
                errorMessage,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun onSubmitFilter(filter: FilterCharacters) {
        charactersListViewModel.onFiltersChange(filter)
        /*scrollToTop()*/
    }

    private fun scrollToTop() {
        binding.characterRecyclerview.smoothScrollToPosition(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}