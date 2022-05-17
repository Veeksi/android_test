package com.example.testapplication.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.testapplication.R
import com.example.testapplication.databinding.FragmentFilterDialogBinding
import com.example.testapplication.domain.model.CharacterGender
import com.example.testapplication.domain.model.CharacterStatus
import com.example.testapplication.domain.model.FilterCharacters
import com.example.testapplication.vm.CharactersListViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FilterDialogFragment : DialogFragment() {
    private var _binding: FragmentFilterDialogBinding? = null
    private val binding get() = _binding!!

    private val charactersListViewModel: CharactersListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val genders = resources.getStringArray(R.array.gender)
        val arrayAdapter = ArrayAdapter(
            requireContext(), R.layout.item_gender_dropdown, genders
        )
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        binding.apply {
            filterButton.setOnClickListener {
                val selectedIndex = statusRadioButtonGroup.indexOfChild(
                    statusRadioButtonGroup.findViewById(
                        statusRadioButtonGroup.checkedRadioButtonId
                    )
                )
                charactersListViewModel.onFiltersChange(
                    FilterCharacters(
                        status = CharacterStatus.values()[selectedIndex],
                        name = nameTextInputEditText.text.toString(),
                        gender = CharacterGender.valueOf(
                            autoCompleteTextView.text.toString().uppercase()
                        ),
                    )
                )
                this@FilterDialogFragment.dismiss()
            }

            cancelButton.setOnClickListener {
                this@FilterDialogFragment.dismiss()
            }
        }
    }

    private fun setupObservers() {
        charactersListViewModel.filterCharactersFlow.value.apply {
            with(binding) {
                (statusRadioButtonGroup.getChildAt(
                    this@apply.status.ordinal
                ) as RadioButton).isChecked = true
                nameTextInputEditText.setText(this@apply.name)
                autoCompleteTextView.setText(this@apply.gender.identifier)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}