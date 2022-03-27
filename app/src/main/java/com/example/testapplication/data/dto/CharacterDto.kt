package com.example.testapplication.data.dto

import com.example.testapplication.domain.model.Character

data class CharacterDto(
    val created: String,
    val episode: List<String>,
    val gender: String,
    val id: Int,
    val image: String,
    val location: LocationDto,
    val name: String,
    val origin: OriginDto,
    val species: String,
    val status: String,
    val type: String,
    val url: String
) {
    fun toCharacter(): Character {
        return Character(
            id = id,
            name = name,
            image = image,
            gender = gender,
        )
    }
}