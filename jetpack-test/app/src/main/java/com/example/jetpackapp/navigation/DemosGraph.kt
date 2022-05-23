package com.example.jetpackapp.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.example.jetpackapp.ui.screens.DemoListScreen
import com.example.jetpackapp.ui.screens.TestScreen1
import com.example.jetpackapp.ui.screens.TestScreen2
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.demosGraph(navController: NavHostController) {
    navigation(
        startDestination = BottomNavItem.Demos.Route,
        route = DEMO_GRAPH_ROUTE
    ) {
        composable(
            route = BottomNavItem.Demos.Route
        ) {
            DemoListScreen(navController)
        }
        composable(
            route = Screens.Test1.route,
        ) {
            TestScreen1()
        }
        composable(
            route = Screens.Test2.route,
        ) {
            TestScreen2()
        }
    }
}