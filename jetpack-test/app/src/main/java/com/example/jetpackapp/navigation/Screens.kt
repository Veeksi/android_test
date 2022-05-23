package com.example.jetpackapp.navigation

import androidx.navigation.NamedNavArgument

const val ROOT_GRAPH_ROUTE = "root_graph"
const val CHARACTERS_GRAPH_ROUTE = "characters_graph"
const val DEMO_GRAPH_ROUTE = "demos_graph"

sealed class Screens(val route: String) {
    object Test1 : Screens("test1")
    object Test2 : Screens("test2")
    object Character : Screens("character/character_screen?id={id}") {
        fun passId(
            id: Int? = 0,
        ): String {
            return "character/character_screen?id=$id"
        }
    }
}
