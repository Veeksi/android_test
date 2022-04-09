package com.example.testapplication.data.dto

import com.example.testapplication.domain.model.Location

data class LocationDto(
    val name: String,
) {
    fun toLocation(): Location {
        return Location(
            name = name
        )
    }
}