package com.example.testapplication.view.fragment

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.os.Bundle
import android.text.method.Touch.onTouchEvent
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.selection.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.R
import com.example.testapplication.databinding.FragmentCharactersListBinding
import com.example.testapplication.domain.model.Character
import com.example.testapplication.util.PagerEvents
import com.example.testapplication.util.PagingLoadStateAdapter
import com.example.testapplication.util.navigate
import com.example.testapplication.view.adapter.CharacterListAdapter
import com.example.testapplication.view.adapter.ItemDetailsLookUp
import com.example.testapplication.view.adapter.ItemsKeyProvider
import com.example.testapplication.vm.CharactersListViewModel
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException


@AndroidEntryPoint
class CharactersListFragment : BaseFragment<FragmentCharactersListBinding>() {
    private val charactersListViewModel: CharactersListViewModel by activityViewModels()
    private lateinit var characterListAdapter: CharacterListAdapter

    private var tracker: SelectionTracker<Character>? = null

    override fun getViewBinding() = FragmentCharactersListBinding.inflate(layoutInflater)

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        tracker?.setItemsSelected(charactersListViewModel.editState.value.editableCharacters, true)
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupCustomUpHandler()
    }

    private fun setupCustomUpHandler() {
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (charactersListViewModel.editState.value.isEditing) {
                    stopEditing()
                } else if (binding.characterRecyclerview.canScrollVertically(-1)) {
                    scrollToTop()
                } else {
                    isEnabled = false
                    activity?.onBackPressed()
                }
            }
        })
    }

    private fun setupAppbar() {
        binding.apply {
            toolbar.inflateMenu(R.menu.filter_menu)
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.filter -> {
                        val dialogFragment = FilterDialogFragment()
                        dialogFragment.show(childFragmentManager, "filter")
                        true
                    }
                    R.id.like -> {
                        charactersListViewModel.addCharactersToFavorites(charactersListViewModel.editState.value.editableCharacters)
                        stopEditing()
                        true
                    }
                    R.id.remove -> {
                        charactersListViewModel.editState.value.editableCharacters.map { character ->
                            charactersListViewModel.onViewEvent(
                                PagerEvents.Remove(character)
                            )
                        }
                        stopEditing()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        setupAppbar()
        setupUi()
        setupObservers()
        // Postpones return transition when the view is ready
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    private fun characterItemClicked(
        character: Character,
        card: MaterialCardView,
    ) {
        val extras = FragmentNavigatorExtras(
            card to "${character.id}-${character.image}"
        )
        val action = CharactersListFragmentDirections
            .actionCharacterListFragmentToCharacterFragment(
                id = character.id,
                uri = character.image,
                name = character.name,
            )

        navigate(action, extras)
    }

    private fun setupUi() {
        characterListAdapter = CharacterListAdapter(::characterItemClicked)

        with(binding) {
            characterRecyclerview.apply {
                setHasFixedSize(true)
                val layoutManager = GridLayoutManager(context, 4)
                if (activity?.resources?.configuration?.orientation == ORIENTATION_PORTRAIT) {
                    layoutManager.spanCount = 2
                }
                characterRecyclerview.layoutManager = layoutManager


                val footerAdapter = PagingLoadStateAdapter(characterListAdapter)
                adapter = characterListAdapter.withLoadStateFooter(
                    footer = footerAdapter
                )

                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (position == characterListAdapter.itemCount && footerAdapter.itemCount > 0) {
                            if (activity?.resources?.configuration?.orientation == ORIENTATION_PORTRAIT) {
                                2
                            } else {
                                4
                            }
                        } else {
                            1
                        }
                    }
                }

                tracker = SelectionTracker.Builder(
                    "selectionItem",
                    binding.characterRecyclerview,
                    ItemsKeyProvider(characterListAdapter),
                    ItemDetailsLookUp(binding.characterRecyclerview),
                    StorageStrategy.createParcelableStorage(Character::class.java)
                ).withSelectionPredicate(
                    SelectionPredicates.createSelectAnything()
                ).build()

                tracker?.addObserver(
                    object : SelectionTracker.SelectionObserver<Character>() {
                        override fun onItemStateChanged(key: Character, selected: Boolean) {
                            super.onItemStateChanged(key, selected)
                            charactersListViewModel.changeSelection(key, selected)
                        }

                        override fun onSelectionChanged() {
                            super.onSelectionChanged()
                            tracker?.let {
                                charactersListViewModel.startEditing()
                                if (!it.hasSelection()) {
                                    charactersListViewModel.stopEditing()
                                }
                            }
                        }
                    }
                )

                characterListAdapter.tracker = tracker

                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(
                        recyclerView: RecyclerView,
                        newState: Int
                    ) {
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

            toolbar.setNavigationOnClickListener {
                stopEditing()
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

            launchWhenStarted {
                charactersListViewModel.editState.collectLatest { editState ->
                    with(binding) {
                        if (editState.isEditing) {
                            binding.toolbar.title = "${editState.editableCharacters.size} selected"
                            toolbar.setNavigationIcon(R.drawable.ic_close)
                            toolbar.setNavigationOnClickListener { stopEditing() }
                            swipeRefreshLayout.isEnabled = false
                        } else {
                            toolbar.title = "Characters"
                            toolbar.navigationIcon = null
                            swipeRefreshLayout.isEnabled = true
                        }

                        toolbar.menu.findItem(R.id.filter).isVisible = !editState.isEditing
                        toolbar.menu.findItem(R.id.like).isVisible = editState.isEditing
                        toolbar.menu.findItem(R.id.remove).isVisible = editState.isEditing
                    }
                }
            }

            launch {
                characterListAdapter.loadStateFlow.collectLatest { loadState ->
                    with(binding) {
                        showErrorToast(loadState)
                        swipeRefreshLayout.isRefreshing = false
                        circularProgressIndicator.isVisible =
                            loadState.refresh is LoadState.Loading && characterListAdapter.itemCount == 0
                        errorMessage.isVisible =
                            loadState.refresh is LoadState.Error
                                    && characterListAdapter.itemCount == 0
                        characterRecyclerview.isVisible =
                            loadState.source.refresh is LoadState.NotLoading
                                    || loadState.mediator?.refresh is LoadState.NotLoading
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

    private fun scrollToTop() {
        binding.characterRecyclerview.smoothScrollToPosition(0)
    }

    private fun stopEditing() {
        tracker?.setItemsSelected(
            charactersListViewModel.editState.value.editableCharacters,
            false
        )
        charactersListViewModel.stopEditing()
    }
}
