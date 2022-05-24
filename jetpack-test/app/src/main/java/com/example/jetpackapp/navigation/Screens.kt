package com.example.jetpackapp.navigation

const val ROOT_GRAPH_ROUTE = "root_graph"
const val CHARACTERS_GRAPH_ROUTE = "characters_graph"
const val DEMO_GRAPH_ROUTE = "demos_graph"

sealed class Screens(val route: String) {
    object Test1 : Screens("demos/test1")
    object Test2 : Screens("demos/test2")
    object Character : Screens("characters/character?id={id}") {
        fun passId(
            id: Int? = 0,
        ): String {
            return "characters/character?id=$id"
        }
    }
}
