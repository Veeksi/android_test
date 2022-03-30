package com.example.testapplication.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.testapplication.databinding.CharacterItemBinding
import com.example.testapplication.domain.model.Character

class CharacterAdapter (
    private val onCharacterItemClicked: (Character) -> Unit
) : PagingDataAdapter<Character, CharacterAdapter.ViewHolder>(CharacterComparator) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CharacterItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ViewHolder(private val binding: CharacterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(character: Character) {
            binding.apply {
                character.also { (_, name, image, gender, liked) ->
                    nameTextview.text = name
                    imageImageview.load(image)
                    genderTextview.text = gender
                    likeText.text = liked.toString()
                }

                characterItem.setOnClickListener {
                    onCharacterItemClicked(character)
                }
            }
        }
    }

    object CharacterComparator : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Character, newItem: Character) =
            oldItem == newItem
    }
}