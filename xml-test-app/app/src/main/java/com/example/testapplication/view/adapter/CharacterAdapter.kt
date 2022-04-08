package com.example.testapplication.view.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.example.testapplication.R
import com.example.testapplication.databinding.CharacterItemBinding
import com.example.testapplication.domain.model.Character


class CharacterAdapter(
    private val fragment: Fragment,
    private val onCharacterItemClicked: (Character, ImageView) -> Unit,
) : PagingDataAdapter<Character, CharacterAdapter.ViewHolder>(CharacterComparator) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CharacterItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("TAG", "Setting transition name")
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ViewHolder(private val binding: CharacterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(character: Character) {
            binding.apply {
                character.also { (id, name, image, gender, liked) ->
                    nameTextview.text = name
                    imageView.apply {
                        transitionName = "$id-$image"
                        load(image){
                            allowHardware(false)
                            memoryCacheKey(image)
                            crossfade(true)
                            placeholder(R.drawable.ic_launcher_foreground)
                            scale(Scale.FILL)
                        }
                    }
                    genderTextview.text = gender
                    likeText.text = liked.toString()
                }

                characterItem.setOnClickListener {
                    onCharacterItemClicked(character, binding.imageView)
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