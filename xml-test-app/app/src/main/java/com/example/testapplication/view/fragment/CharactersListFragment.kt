package com.example.testapplication.view.fragment

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.*
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
import com.example.testapplication.util.PagingLoadStateAdapter
import com.example.testapplication.view.adapter.CharacterListAdapter
import com.example.testapplication.vm.CharactersListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException


@AndroidEntryPoint
class CharactersListFragment : BaseFragment<FragmentCharactersListBinding>() {
    private val charactersListViewModel: CharactersListViewModel by activityViewModels()
    private lateinit var characterListAdapter: CharacterListAdapter

    override fun getViewBinding() = FragmentCharactersListBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (charactersListViewModel.isEditing.value == true) {
                    charactersListViewModel.stopEditing()
                    requireActivity().invalidateOptionsMenu()
                } else if (binding.characterRecyclerview.canScrollVertically(-1)) {
                    scrollToTop()
                } else {
                    isEnabled = false
                    activity?.onBackPressed()
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val filterItem = menu.findItem(R.id.filter)
        val likeItem = menu.findItem(R.id.like)
        val removeItem = menu.findItem(R.id.remove)
        filterItem.isVisible = !charactersListViewModel.isEditing.value!!
        likeItem.isVisible = charactersListViewModel.isEditing.value ?: false
        removeItem.isVisible = charactersListViewModel.isEditing.value ?: false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filter -> {
                val dialogFragment = FilterDialogFragment()
                dialogFragment.show(childFragmentManager, "filter")
                true
            }
            R.id.like -> {
                /*val dialogFragment = FilterDialogFragment()
                dialogFragment.show(childFragmentManager, "filter")*/
                true
            }
            R.id.remove -> {
                /*val dialogFragment = FilterDialogFragment()
                dialogFragment.show(childFragmentManager, "filter")*/
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

        findNavController().navigate(action, extras)
    }

    private fun characterItemLongClicked(character: Character) {
        charactersListViewModel.startEditing()
        requireActivity().invalidateOptionsMenu()
    }

    private fun setupUi() {
        characterListAdapter =
            CharacterListAdapter(::characterItemClicked, ::characterItemLongClicked)

        with(binding) {
            characterRecyclerview.apply {
                layoutManager =
                    if (activity?.resources?.configuration?.orientation == ORIENTATION_PORTRAIT) {
                        GridLayoutManager(context, 3)
                    } else {
                        GridLayoutManager(context, 4)
                    }
                setHasFixedSize(true)
                adapter = characterListAdapter.withLoadStateHeaderAndFooter(
                    header = PagingLoadStateAdapter(characterListAdapter),
                    footer = PagingLoadStateAdapter(characterListAdapter)
                )

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

    private fun scrollToTop() {
        binding.characterRecyclerview.smoothScrollToPosition(0)
    }
}