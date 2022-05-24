package com.example.testapplication.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testapplication.databinding.FragmentFavoriteCharactersBinding
import com.example.testapplication.view.fragment.BaseFragment

class FavoriteCharactersFragment : BaseFragment<FragmentFavoriteCharactersBinding>() {

    override fun getViewBinding() = FragmentFavoriteCharactersBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}