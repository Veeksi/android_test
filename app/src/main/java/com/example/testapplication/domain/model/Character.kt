package com.example.testapplication.domain.model

import com.google.gson.annotations.SerializedName

data class Characters (
    val results: List<Character>
        )

data class Character(
    val name: String,
    val image: String,
    val gender: String
) {
    sealed class Gender {
        object Male: Gender()
        object Female: Gender()
    }
}
