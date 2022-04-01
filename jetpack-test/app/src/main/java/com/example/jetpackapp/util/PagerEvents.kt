package com.example.jetpackapp.util

import com.example.jetpackapp.domain.model.Character

sealed class PagerEvents {
    data class Like(val character: Character): PagerEvents()
    data class Remove(val character: Character) : PagerEvents()
}