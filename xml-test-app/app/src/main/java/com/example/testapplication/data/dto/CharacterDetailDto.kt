package com.example.testapplication.data.dto

import android.util.Log
import com.example.testapplication.domain.model.Character
import com.google.gson.annotations.SerializedName

data class CharacterDetailDto(
    val id: Int,
    val name: String,
    val image: String,
    val gender: String,
    val location: LocationDto,
    val created: String,
    val origin: OriginDto,
    val species: String,
    val status: String,
    val type: String,
    @SerializedName("episode")
    val episodes: List<String>,
) {
    fun toCharacter(): Character {
        return Character(
            id = id,
            name = name,
            image = image,
            gender = gender,
            location = location.toLocation(),
            created = created,
            origin = origin.toOrigin(),
            species = species,
            status = status,
            type = type,
            episodes = episodes,
        )
    }
}
