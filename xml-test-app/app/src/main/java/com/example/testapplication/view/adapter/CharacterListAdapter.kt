package com.example.testapplication.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.testapplication.R
import com.example.testapplication.databinding.ItemCharacterBinding
import com.example.testapplication.domain.model.Character
import com.google.android.material.card.MaterialCardView

class ItemDetailsLookUp(
    private val recyclerView: RecyclerView
) : ItemDetailsLookup<Character>() {
    override fun getItemDetails(event: MotionEvent): ItemDetails<Character>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)
        if (view != null) {
            return (recyclerView.getChildViewHolder(view) as CharacterListAdapter.ViewHolder)
                .getItemDetails()
        }
        return null
    }
}

class ItemsKeyProvider(private val adapter: CharacterListAdapter) : ItemKeyProvider<Character>(
    SCOPE_CACHED
) {
    override fun getKey(position: Int): Character? {
        return adapter.snapshot().items[position]
    }

    override fun getPosition(key: Character): Int {
        return adapter.snapshot().items.indexOfFirst { it == key }
    }
}

class CharacterListAdapter(
    private val onCharacterItemClicked: (Character, MaterialCardView) -> Unit,
) : PagingDataAdapter<Character, CharacterListAdapter.ViewHolder>(CharacterComparator) {

    var tracker: SelectionTracker<Character>? = null

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

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Character> =
            object : ItemDetailsLookup.ItemDetails<Character>() {
                override fun getPosition(): Int = bindingAdapterPosition
                override fun getSelectionKey(): Character = snapshot().items[position]
            }

        fun bind(character: Character) {
            binding.apply {
                character.also { (id, name, image) ->
                    tracker?.let {
                        cardView.isChecked = it.isSelected(character)
                    }
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
                cardView.setOnClickListener {
                    onCharacterItemClicked(character, binding.cardView)
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