package com.example.testapplication.domain.model

data class FilterCharacters(
    val status: CharacterStatus = CharacterStatus.ALL,
    val name: String = "",
    val gender: CharacterGender = CharacterGender.ALL,
)

enum class CharacterStatus(val value: String) {
    ALL(""),
    ALIVE("alive"),
    DEAD("dead"),
    UNKNOWN("unknown")
}

enum class CharacterGender(val value: String, val identifier: String) {
    ALL("", "All"),
    FEMALE("female", "Female"),
    MALE("male", "Male"),
    GENDERLESS("genderless", "Genderless"),
    UNKNOWN("unknown", "Unknown")
}
