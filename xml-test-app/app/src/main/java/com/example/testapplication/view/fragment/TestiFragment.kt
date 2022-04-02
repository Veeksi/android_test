package com.example.testapplication.view.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.testapplication.R
import com.example.testapplication.databinding.FragmentTestiBinding
import com.example.testapplication.view.MainActivity

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class TestiFragment : Fragment() {
    private var _binding: FragmentTestiBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.title = "Testi"
        _binding = FragmentTestiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.toolbar.setNavigationIcon(R.drawable.ic_back)
        setHasOptionsMenu(true)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    /*

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (requireActivity() as MainActivity).supportActionBar?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as MainActivity).supportActionBar?.show()
        _binding = null
    }

     */
}