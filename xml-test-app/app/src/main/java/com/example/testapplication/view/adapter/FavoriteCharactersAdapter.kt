package com.example.testapplication.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.testapplication.R
import com.example.testapplication.databinding.ItemCharacterBinding
import com.example.testapplication.domain.model.Character
import com.google.android.material.card.MaterialCardView

class FavoriteCharactersAdapter(
    private val onCharacterItemClicked: (Character) -> Unit,
) : ListAdapter<Character, FavoriteCharactersAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ViewHolder(private val binding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(character: Character) {
            binding.apply {
                character.also { (id, name, image) ->
                    cardView.layoutParams.width = 360
                    characterItem.maxWidth = 360
                    cardView.isClickable = false
                    cardView.isFocusable = false
                    title.text = name
                    imageView.apply {
                        load(image) {
                            allowHardware(false)
                            memoryCacheKey(image)
                            crossfade(true)
                            placeholder(R.drawable.ic_launcher_foreground)
                            scale(Scale.FILL)
                        }
                    }
                    likeButton.visibility = View.VISIBLE
                    likeButton.setOnClickListener {
                        onCharacterItemClicked(character)
                    }
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Character, newItem: Character) =
            oldItem == newItem
    }
}