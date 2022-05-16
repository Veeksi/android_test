package com.example.testapplication.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.databinding.ItemNavigationBinding
import com.example.testapplication.domain.model.NavigationItem

class PageAdapter(
    private val onNavigationItemCLicked: (NavigationItem) -> Unit,
) : ListAdapter<NavigationItem, PageAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNavigationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ViewHolder(private val binding: ItemNavigationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(navigationItem: NavigationItem) {
            binding.apply {
                title.text = navigationItem.title
                description.text = navigationItem.description
                cardView.setOnClickListener {
                    onNavigationItemCLicked(navigationItem)
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<NavigationItem>() {
        override fun areItemsTheSame(oldItem: NavigationItem, newItem: NavigationItem) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: NavigationItem, newItem: NavigationItem) =
            oldItem == newItem
    }
}