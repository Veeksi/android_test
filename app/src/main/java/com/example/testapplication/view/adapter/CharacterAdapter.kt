package com.example.testapplication.view.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.testapplication.databinding.CharacterItemBinding
import com.example.testapplication.domain.model.Character
import javax.inject.Inject

class CharacterAdapter @Inject constructor() : RecyclerView.Adapter<CharacterAdapter.ViewHolder>() {
    var characters: List<Character> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CharacterItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(characters[position])
    }

    override fun getItemCount(): Int = characters.size

    inner class ViewHolder(private val binding: CharacterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(character: Character) {
            binding.apply {
                character.also { (name, image, gender) ->
                    nameTextview.text = name
                    imageImageview.load(image)
                    genderTextview.text = gender
                }
            }
        }
    }
}