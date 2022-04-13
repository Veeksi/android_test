package com.example.testapplication.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.testapplication.R
import com.example.testapplication.databinding.FragmentCharacterBinding
import com.example.testapplication.util.Resource
import com.example.testapplication.util.setMotionVisibility
import com.example.testapplication.view.adapter.EpisodeListAdapter
import com.example.testapplication.vm.CharacterViewModel
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CharacterFragment : Fragment() {
    private val args: CharacterFragmentArgs by navArgs()
    private val characterViewModel: CharacterViewModel by viewModels()
    private lateinit var episodeListAdapter: EpisodeListAdapter
    private var _binding: FragmentCharacterBinding? = null

    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            duration = resources.getInteger(R.integer.anim_duration_long).toLong()
            scrimColor = Color.TRANSPARENT
            setPathMotion(MaterialArcMotion())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCharacterBinding.inflate(inflater, container, false)
        return binding.root
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
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
            }
            characterImage.apply {
                transitionName = "${args.id}-${args.uri}"
                load(args.uri)
            }
            characterName.apply {
                transitionName = "${args.id}-${args.name}"
                text = args.name
            }
        }
    }

    private fun setupObservers() {
        characterViewModel.episodes.observe(viewLifecycleOwner) { result ->
            episodeListAdapter.submitList(result)
        }

        characterViewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            if (loading == false) {
                binding.loadingIndicator.setMotionVisibility(View.GONE)
            }
        }

        characterViewModel.character.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Success -> {
                    with(binding) {
                        result.data?.let { character ->
                            characterName.text = getString(
                                R.string.character_name,
                                character.name,
                                character.id
                            )
                        }
                    }
                }
                is Resource.Loading -> {
                    with(binding) {
                        loadingIndicator.setMotionVisibility(View.VISIBLE)
                    }
                }
                is Resource.Error -> {
                    with(binding) {
                        errorMessage.text = result.message
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

