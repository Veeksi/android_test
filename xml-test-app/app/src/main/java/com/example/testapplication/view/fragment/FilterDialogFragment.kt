package com.example.testapplication.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.RadioButton
import androidx.fragment.app.DialogFragment
import com.example.testapplication.R
import com.example.testapplication.databinding.FragmentFilterDialogBinding
import com.example.testapplication.domain.model.CharacterGender
import com.example.testapplication.domain.model.CharacterStatus
import com.example.testapplication.domain.model.FilterCharacters
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FilterDialogFragment(
    val previousFilters: FilterCharacters,
    val onSubmitFilter: (filter: FilterCharacters) -> Unit,
) : DialogFragment() {
    private var _binding: FragmentFilterDialogBinding? = null

    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupInitialValues()
        binding.apply {
            filterButton.setOnClickListener {
                val selectedIndex = statusRadioButtonGroup.indexOfChild(
                    statusRadioButtonGroup.findViewById(
                        statusRadioButtonGroup.checkedRadioButtonId
                    )
                )
                onSubmitFilter(
                    FilterCharacters(
                        status = CharacterStatus.values()[selectedIndex],
                        name = nameTextInputEditText.text.toString(),
                        gender = CharacterGender.valueOf(autoCompleteTextView.text.toString().uppercase()),
                    )
                )
                this@FilterDialogFragment.dismiss()
            }

            val genders = resources.getStringArray(R.array.gender)
            val arrayAdapter = ArrayAdapter(
                requireContext(), R.layout.item_gender_dropdown, genders
            )
            binding.autoCompleteTextView.setAdapter(arrayAdapter)

            cancelButton.setOnClickListener {
                this@FilterDialogFragment.dismiss()
            }
        }
    }

    private fun setupInitialValues() {
        binding.apply {
            (statusRadioButtonGroup.getChildAt(
                previousFilters.status.ordinal
            ) as RadioButton).isChecked = true
            nameTextInputEditText.setText(previousFilters.name)
            autoCompleteTextView.setText(previousFilters.gender.identifier)
        }
    }
}