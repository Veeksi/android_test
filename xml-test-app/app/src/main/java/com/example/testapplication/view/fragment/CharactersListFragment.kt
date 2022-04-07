package com.example.testapplication.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.testapplication.R
import com.example.testapplication.databinding.FragmentCharactersListBinding
import com.example.testapplication.domain.model.Character
import com.example.testapplication.util.PagerEvents
import com.example.testapplication.util.PagingLoadStateAdapter
import com.example.testapplication.view.adapter.CharacterAdapter
import com.example.testapplication.vm.CharactersListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class CharactersListFragment : Fragment() {
    private val charactersListViewModel: CharactersListViewModel by viewModels()
    private var _binding: FragmentCharactersListBinding? = null

    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    private lateinit var characterAdapter: CharacterAdapter

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
        postponeEnterTransition(5, TimeUnit.MILLISECONDS)
        setupUi()
        view.doOnPreDraw {
            Log.d("TAG", "Start postpone")
            startPostponedEnterTransition()
        }


        setupObservers()
    }

    private fun characterItemClicked(character: Character, imageView: ImageView) {
        val extras = FragmentNavigatorExtras(
            imageView to "${character.id}-${character.image}"
        )

        val bundle = Bundle()
        bundle.putInt("id", character.id)
        bundle.putString("image", character.image)

        val modalBottomSheet = ModalBottomSheet(
            onView = {
                findNavController().navigate(
                    R.id.action_characterListFragment_to_characterFragment,
                    bundle,
                    null,
                    extras,
                )
            },
            onLike = { charactersListViewModel.onViewEvent(PagerEvents.Like(character)) },
            onDelete = { charactersListViewModel.onViewEvent(PagerEvents.Remove(character)) }
        )
        if (childFragmentManager.findFragmentByTag("dialogTAG") == null) {
            modalBottomSheet.show(childFragmentManager, "dialogTAG")
        }
    }

    fun ImageView.load(url: String) {
        Glide.with(context).asBitmap()
            .load(url)
            .apply(RequestOptions.placeholderOf(R.drawable.ic_launcher_foreground))
            .into(this)
    }

    private fun setupUi() {
        characterAdapter = CharacterAdapter(
            onCharacterItemClicked = { character, imageView ->
                characterItemClicked(character, imageView)
            },
            fragment = this
        )

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
                    // startPostponedEnterTransition()
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