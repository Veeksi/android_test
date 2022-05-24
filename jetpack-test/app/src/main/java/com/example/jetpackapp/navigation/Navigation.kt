package com.example.jetpackapp.navigation

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController,
        startDestination = CHARACTERS_GRAPH_ROUTE,
        route = ROOT_GRAPH_ROUTE,
        modifier = modifier
    ) {
        charactersGraph(navController = navController)
        demosGraph(navController = navController)
    }
}