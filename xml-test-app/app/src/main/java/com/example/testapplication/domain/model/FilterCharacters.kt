package com.example.testapplication.domain.model

data class FilterCharacters(
    val status: CharacterStatus = CharacterStatus.ALL,
    val name: String = "",
)

enum class CharacterStatus(val value: String, val identifier: String) {
    ALL("", "ALL"),
    ALIVE("alive", "ALIVE"),
    DEAD("dead", "DEAD"),
    UNKNOWN("unknown", "UNKNOWN")
}
