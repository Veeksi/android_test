package com.example.testapplication.view.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.testapplication.R
import com.example.testapplication.databinding.CharacterItemBinding
import com.example.testapplication.domain.model.Character

interface OnImageReadyListener {
    fun onImageReady(position: Int)
}

class CharacterAdapter(
    private val onCharacterItemClicked: (Character, ImageView) -> Unit,
    private val fragment: Fragment,
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
        getItem(position)?.let { holder.bind(it) }
    }


    fun ImageView.load(url: String, fragment: Fragment) {
        Glide.with(fragment.requireActivity()).asBitmap()
            .dontTransform()
            .disallowHardwareConfig()
            .load(url)
            .apply(RequestOptions.placeholderOf(R.drawable.ic_launcher_foreground))
            .into(this)
    }

    inner class ViewHolder(private val binding: CharacterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(character: Character) {
            binding.apply {
                character.also { (id, name, image, gender, liked) ->
                    Log.d("TAG", image)
                    nameTextview.text = name
                    imageView.apply {
                        load(image, fragment)
                        transitionName = "$id-$image"
                        /*{

                            allowHardware(false)
                            memoryCacheKey(image)
                            crossfade(true)
                            placeholder(R.drawable.ic_launcher_foreground)
                            transformations(CircleCropTransformation())
                            scale(Scale.FILL)
                        }

                         */
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