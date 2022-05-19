package com.example.testapplication.data.dto

import com.example.testapplication.domain.model.Character

data class CharacterDto(
    val id: Int,
    val name: String,
    val image: String,
    val gender: String,
    val status: String,
) {
    fun toCharacter(): Character {
        return Character(
            id = id,
            name = name,
            image = image,
            gender = gender,
            status = status
        )
    }
}