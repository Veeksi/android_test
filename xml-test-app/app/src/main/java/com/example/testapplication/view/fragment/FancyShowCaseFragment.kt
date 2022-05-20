package com.example.testapplication.view.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.View
import androidx.activity.addCallback
import androidx.core.view.MenuItemCompat
import com.example.testapplication.R
import com.example.testapplication.databinding.FragmentFancyShowCaseBinding
import me.toptas.fancyshowcase.FancyShowCaseQueue
import me.toptas.fancyshowcase.FancyShowCaseView


class FancyShowCaseFragment : BaseFragment<FragmentFancyShowCaseBinding>() {

    override fun getViewBinding() = FragmentFancyShowCaseBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fancyShowCaseView1: FancyShowCaseView = FancyShowCaseView.Builder(requireActivity())
            .title("Here is a button that triggers this show case")
            .enableAutoTextPosition()
            .titleGravity(Gravity.CENTER)
            .focusOn(binding.button)
            .build()


        val fancyShowCaseView2: FancyShowCaseView = FancyShowCaseView.Builder(requireActivity())
            .title("Here is a toolbar that you can use to make actions (not really)")
            .titleStyle(0, Gravity.CENTER)
            .enableAutoTextPosition()
            .fitSystemWindows(true)
            .focusOn(binding.toolbar)
            .build()

        val queue = FancyShowCaseQueue()

        binding.button.setOnClickListener {
            queue.add(fancyShowCaseView1).add(fancyShowCaseView2)
            queue.show()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (FancyShowCaseView.isVisible(requireActivity())) {
                queue.cancel()
            } else {
                this.isEnabled = false
                activity?.onBackPressed()
            }
        }
    }
}