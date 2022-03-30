package com.example.testapplication.util

import com.example.testapplication.domain.model.Character

sealed class PagerEvents {
    data class Like(val character: Character): PagerEvents()
    data class Remove(val character: Character) : PagerEvents()
}