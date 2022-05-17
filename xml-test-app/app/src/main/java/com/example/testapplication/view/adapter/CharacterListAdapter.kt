package com.example.testapplication.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.testapplication.R
import com.example.testapplication.databinding.ItemCharacterBinding
import com.example.testapplication.domain.model.Character


class CharacterListAdapter(
    private val onCharacterItemClicked: (Character, CardView) -> Unit,
    private val onCharacterItemLongClicked: (Character, ImageView) -> Unit,
) : PagingDataAdapter<Character, CharacterListAdapter.ViewHolder>(CharacterComparator) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ViewHolder(private val binding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(character: Character) {
            binding.apply {
                character.also { (id, name, image, _, _) ->
                    cardView.transitionName = "$id-$image"
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
                }
                cardView.setOnClickListener { onCharacterItemClicked(character, binding.cardView) }
                cardView.setOnLongClickListener {
                    onCharacterItemLongClicked(character, binding.selectedIcon)
                    true
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