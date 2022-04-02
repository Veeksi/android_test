package com.example.testapplication.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testapplication.R
import com.example.testapplication.databinding.ModalBottomSheetContentBinding
import com.example.testapplication.domain.model.Character
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ModalBottomSheet(
    private val onView: () -> Unit,
    private val onLike: () -> Unit,
    private val onDelete: () -> Unit,
): BottomSheetDialogFragment() {
    private var _binding: ModalBottomSheetContentBinding? = null
    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

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
                onView()
                dismiss()
            }
            likeListTile.setOnClickListener {
                onLike()
                dismiss()
            }
            deleteListTile.setOnClickListener {
                onDelete()
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}