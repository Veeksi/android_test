package com.example.testapplication.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.R
import com.example.testapplication.databinding.ItemEpisodeBinding
import com.example.testapplication.databinding.ItemEpisodeHeaderBinding
import com.example.testapplication.domain.model.Episode
import com.example.testapplication.domain.model.EpisodeDataItem
import java.lang.IllegalArgumentException

class EpisodeListAdapter : ListAdapter<EpisodeDataItem, RecyclerView.ViewHolder>(EpisodeDiffUtil) {

    companion object {
        private const val ITEM_VIEW_TYPE_HEADER = 0
        private const val ITEM_VIEW_TYPE_ITEM = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder(
                ItemEpisodeHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            ITEM_VIEW_TYPE_ITEM -> ViewHolder(
                ItemEpisodeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EpisodeListAdapter.ViewHolder -> {
                val item = getItem(position) as EpisodeDataItem.EpisodeItem
                holder.bind(item.episode)
            }
            is EpisodeListAdapter.HeaderViewHolder -> {
                val item = getItem(position) as EpisodeDataItem.EpisodeHeader
                holder.bind(item.episodeCount)
            }
            else -> throw  IllegalArgumentException("Unknown viewHolder $holder")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is EpisodeDataItem.EpisodeHeader -> ITEM_VIEW_TYPE_HEADER
            is EpisodeDataItem.EpisodeItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    inner class HeaderViewHolder(private val binding: ItemEpisodeHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(episodeCount: Int) {
            binding.apply {
                episodesText.text = this@HeaderViewHolder.itemView.context.getString(
                    R.string.episode_count,
                    episodeCount
                )
            }
        }
    }

    inner class ViewHolder(private val binding: ItemEpisodeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(episode: Episode) {
            binding.apply {
                episodeName.text = episode.name
                episodeAirDate.text = episode.air_date
                episodeCode.text = episode.episode
            }
        }
    }

    object EpisodeDiffUtil : DiffUtil.ItemCallback<EpisodeDataItem>() {
        override fun areItemsTheSame(oldItem: EpisodeDataItem, newItem: EpisodeDataItem): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(
            oldItem: EpisodeDataItem, newItem: EpisodeDataItem
        ): Boolean = oldItem == newItem
    }
}