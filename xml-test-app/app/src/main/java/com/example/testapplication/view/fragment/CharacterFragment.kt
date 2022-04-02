package com.example.testapplication.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testapplication.R
import com.example.testapplication.databinding.FragmentCharacterBinding

class CharacterFragment : Fragment() {
    private var _binding: FragmentCharacterBinding? = null
    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        val characterId = arguments?.getInt("id").toString()

        characterId.let {
            binding.characterId.text = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}