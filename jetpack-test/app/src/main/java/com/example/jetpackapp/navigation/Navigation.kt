package com.example.jetpackapp.navigation

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.jetpackapp.ui.screens.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation(navController: NavHostController, modifier: Modifier = Modifier) {
    AnimatedNavHost(
        navController,
        startDestination = CHARACTERS_GRAPH_ROUTE,
        route = ROOT_GRAPH_ROUTE,
        modifier = modifier
    ) {
        charactersGraph(navController = navController)
        demosGraph(navController = navController)
    }
}