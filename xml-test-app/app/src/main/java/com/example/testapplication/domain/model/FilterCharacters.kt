package com.example.testapplication.domain.model

data class FilterCharacters(
    val status: CharacterStatus = CharacterStatus.ALL,
    val name: String = "",
    val gender: String = CharacterGender.ALL.value,
)

enum class CharacterStatus(val value: String) {
    ALL(""),
    ALIVE("alive"),
    DEAD("dead"),
    UNKNOWN("unknown")
}

enum class CharacterGender(val value: String) {
    ALL(""),
    FEMALE("female"),
    MALE("male"),
    GENDERLESS("genderless"),
    UNKNOWN("unknown")
}
