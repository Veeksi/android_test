package com.example.testapplication.domain.model

data class Character(
    val id: Int,
    val name: String,
    val image: String,
    val gender: String,
    var liked: Boolean = false,
    val location: Location? = null,
    val created: String? = null,
    val origin: Origin? = null,
    val species: String? = null,
    val status: String? = null,
    val type: String? = null,
    val episodes: List<String>? = null,
)
