package com.example.testapplication.view.fragment

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.testapplication.R
import com.example.testapplication.databinding.FragmentCharacterBinding
import com.example.testapplication.domain.model.EpisodeDataItem
import com.example.testapplication.util.Resource
import com.example.testapplication.util.setMotionVisibility
import com.example.testapplication.view.adapter.EpisodeListAdapter
import com.example.testapplication.vm.CharacterViewModel
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CharacterFragment : BaseFragment<FragmentCharacterBinding>() {
    private val args: CharacterFragmentArgs by navArgs()
    private val characterViewModel: CharacterViewModel by viewModels()
    private lateinit var episodeListAdapter: EpisodeListAdapter

    override fun getViewBinding() = FragmentCharacterBinding.inflate(layoutInflater)

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBundle("motionLayoutState", binding.characterMotionLayout.transitionState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            binding.characterMotionLayout.transitionState =
                savedInstanceState.getBundle("motionLayoutState")
        }
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            duration = resources.getInteger(R.integer.anim_duration_long).toLong()
            scrimColor = Color.TRANSPARENT
            setPathMotion(MaterialArcMotion())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupObservers()
    }

    private fun setupUi() {
        episodeListAdapter = EpisodeListAdapter()
        with(binding) {
            episodeRecyclerView.apply {
                adapter = episodeListAdapter
                var gridManager = GridLayoutManager(context, 4)
                if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    gridManager = GridLayoutManager(context, 2)
                }
                gridManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int) = when (position) {
                        0 -> gridManager.spanCount
                        else -> 1
                    }
                }
                layoutManager = gridManager
                setHasFixedSize(true)
            }
            characterName.text = args.name
            characterImage.apply {
                transitionName = "${args.id}-${args.uri}"
                load(args.uri)
            }
            backButton.setOnClickListener {
                activity?.onBackPressed()
            }
            retryButton.setOnClickListener {
                characterViewModel.loadCharacter()
            }
        }
    }

    private fun setupObservers() {
        characterViewModel.episodes.observe(viewLifecycleOwner) { result ->
            val items = when (result) {
                null -> listOf(EpisodeDataItem.EpisodeHeader(episodeCount = 0))
                else -> listOf(EpisodeDataItem.EpisodeHeader(episodeCount = result.size)) + result.map {
                    EpisodeDataItem.EpisodeItem(
                        it
                    )
                }
            }
            episodeListAdapter.submitList(items)
        }

        characterViewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            with(binding) {
                if (loading == true) {
                    errorBox.setMotionVisibility(View.GONE)
                    loadingIndicator.setMotionVisibility(View.VISIBLE)
                } else {
                    loadingIndicator.setMotionVisibility(View.GONE)
                }
            }
        }

        characterViewModel.character.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Success -> {
                    with(binding) {
                        characterInfoLayout.setMotionVisibility(View.VISIBLE)
                        result.data?.let { character ->
                            characterId.text = character.id.toString()
                            characterGender.text = character.gender
                            characterStatus.text = character.status
                        }
                    }
                }
                is Resource.Error -> {
                    showToast(getString(R.string.internet_error))
                    with(binding) {
                        errorMessage.text = result.message
                        characterInfoLayout.setMotionVisibility(View.GONE)
                        errorBox.setMotionVisibility(View.VISIBLE)
                    }
                }
            }
        }
    }
}

