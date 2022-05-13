package com.example.testapplication.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.DialogFragment
import com.example.testapplication.databinding.FragmentFilterDialogBinding
import com.example.testapplication.domain.model.CharacterStatus
import com.example.testapplication.domain.model.FilterCharacters
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FilterDialogFragment(
    val previousFilters: FilterCharacters
) : DialogFragment() {
    private lateinit var listener: NoticeDialogListener
    private var _binding: FragmentFilterDialogBinding? = null

    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    interface NoticeDialogListener {
        fun onDialogPositiveClick(filter: FilterCharacters)
    }

    fun setCallback(listener: NoticeDialogListener) {
        this.listener = listener
    }

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

        binding.apply {
            (radioButtonGroup.getChildAt(previousFilters.status.ordinal) as RadioButton).isChecked =
                true
            filterButton.setOnClickListener {
                val selectedIndex = radioButtonGroup.indexOfChild(
                    radioButtonGroup.findViewById(
                        radioButtonGroup.checkedRadioButtonId
                    )
                )
                listener.onDialogPositiveClick(
                    FilterCharacters(
                        status = CharacterStatus.values()[selectedIndex]
                    )
                )
                this@FilterDialogFragment.dismiss()
            }
            cancelButton.setOnClickListener {
                this@FilterDialogFragment.dismiss()
            }
        }
    }
}