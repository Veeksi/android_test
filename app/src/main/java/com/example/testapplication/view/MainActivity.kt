package com.example.testapplication.view

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testapplication.databinding.ActivityMainBinding
import com.example.testapplication.view.adapter.CharacterAdapter
import com.example.testapplication.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUi()
        setupObservers()
    }

    private fun setupUi() {
        binding.characterRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = CharacterAdapter()
            addItemDecoration(DividerItemDecoration(this@MainActivity, LinearLayout.VERTICAL))
        }
    }

    private fun setupObservers() {
        mainViewModel.getCharacter().observe(this, { characterList ->
            characterList?.let {
                binding.characterRecyclerview.apply {
                    with(adapter as CharacterAdapter) {
                        characters = it
                        notifyDataSetChanged()
                    }
                }
            }
        })
    }
}