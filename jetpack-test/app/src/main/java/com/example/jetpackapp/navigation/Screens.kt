package com.example.jetpackapp.navigation

import androidx.navigation.NamedNavArgument

sealed class Screens(val route: String) {
    object Home : Screens("Home")
    object Test : Screens("test")
    object Character : Screens("character/{id}") {
        fun passId(
            id: Int? = 0,
        ): String {
            return "character/${id}"
        }
    }
}
