package com.example.testapplication.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionInflater
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.testapplication.R
import com.example.testapplication.databinding.FragmentCharacterBinding
import com.example.testapplication.util.Resource
import com.example.testapplication.vm.CharacterViewModel
import com.google.android.material.transition.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterFragment : Fragment() {
    private val characterViewModel: CharacterViewModel by viewModels()
    private var _binding: FragmentCharacterBinding? = null

    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            // drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.anim_duration_long).toLong()
            // Set the color of the scrim to transparent as we also want to animate the
            // list fragment out of view
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
        val image = arguments?.getString("image")
        val id = arguments?.getInt("id").toString()
        image?.let {
            id.let {
                binding.characterImage.apply {
                    transitionName = "$id-$image"
                    load(image)
                }
            }
        }
    }

    fun ImageView.load(url: String) {
        Glide.with(this)
            .load(url)
            .apply(RequestOptions.placeholderOf(R.drawable.ic_launcher_foreground))
            .into(this)
    }

    private fun setupObservers() {
        characterViewModel.character.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Success -> {
                    with(binding) {
                        result.data?.let { character ->
                            characterId.text = "ID: ${character.id.toString()}"
                            characterGender.text = "Gender: ${character.gender}"
                            loadingIndicator.visibility = View.GONE
                        }
                    }
                }
                is Resource.Loading -> {
                    with(binding) {
                        loadingIndicator.visibility = View.VISIBLE
                    }
                }
                is Resource.Error -> {
                    with(binding) {
                        errorMessage.text = result.message
                        loadingIndicator.visibility = View.GONE
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

