package com.example.testapplication.view.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.testapplication.R
import com.example.testapplication.domain.model.CharacterStatus
import com.example.testapplication.domain.model.FilterCharacters
import com.example.testapplication.vm.CharactersListViewModel
import com.example.testapplication.vm.FilterDialogViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterDialogFragment(
    val previousFilters: FilterCharacters
) : DialogFragment() {
    private lateinit var listener: NoticeDialogListener
    private val filterDialogViewModel: FilterDialogViewModel by activityViewModels()

    interface NoticeDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, filter: FilterCharacters)
    }

    fun setCallback(listener: NoticeDialogListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Filter")
                .setSingleChoiceItems(
                    R.array.filterStatus,
                    CharacterStatus.valueOf(previousFilters.status.identifier).ordinal,
                ) { dialog, which ->
                    filterChange(which)
                }.setPositiveButton("Filter") { _, _ ->
                    listener.onDialogPositiveClick(
                        this,
                        filterDialogViewModel.filterCharactersFlow.value
                    )
                }.setNegativeButton("Cancel") { _, _ ->
                    run {}
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun filterChange(statusValue: Int) {
        filterDialogViewModel.onFilterChange(
            FilterCharacters(
                status = CharacterStatus.values()[statusValue]
            )
        )
    }
}