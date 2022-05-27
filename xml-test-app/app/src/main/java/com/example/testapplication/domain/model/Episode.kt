package com.example.testapplication.domain.model

data class Episode(
    val air_date: String,
    val characters: List<String>,
    val created: String,
    val episode: String,
    val id: Int,
    val name: String,
    val url: String
)

sealed class EpisodeDataItem {
    class EpisodeHeader(val episodeCount: Int) : EpisodeDataItem() {
        override val id = Long.MIN_VALUE
    }

    data class EpisodeItem(val episode: Episode) : EpisodeDataItem() {
        override val id = episode.id.toLong()
    }

    abstract val id: Long
}