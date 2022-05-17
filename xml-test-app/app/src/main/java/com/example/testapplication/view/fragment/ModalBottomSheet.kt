package com.example.testapplication.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.*
import androidx.navigation.fragment.navArgs
import com.example.testapplication.databinding.ModalBottomSheetContentBinding
import com.example.testapplication.util.PagerEvents
import com.example.testapplication.vm.CharactersListViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ModalBottomSheet() : BottomSheetDialogFragment() {
    private var _binding: ModalBottomSheetContentBinding? = null
    private val binding get() = _binding!!

    private val args: ModalBottomSheetArgs by navArgs()

    private val charactersListViewModel: CharactersListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ModalBottomSheetContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        with(binding) {
            viewListTile.setOnClickListener {
                dismiss()
            }
            likeListTile.setOnClickListener {
                charactersListViewModel.onViewEvent(PagerEvents.Like(args.character))
                dismiss()
            }
            deleteListTile.setOnClickListener {
                charactersListViewModel.onViewEvent(PagerEvents.Remove(args.character))
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}