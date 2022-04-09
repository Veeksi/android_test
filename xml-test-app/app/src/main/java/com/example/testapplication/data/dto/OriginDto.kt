package com.example.testapplication.data.dto

import com.example.testapplication.domain.model.Origin

data class OriginDto(
    val name: String,
) {
    fun toOrigin(): Origin {
        return Origin(
            name = name
        )
    }
}