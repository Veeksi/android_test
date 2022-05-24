package com.example.jetpackapp.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.jetpackapp.ui.screens.DemoListScreen
import com.example.jetpackapp.ui.screens.TestScreen1
import com.example.jetpackapp.ui.screens.TestScreen2

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.demosGraph(navController: NavController) {
    navigation(
        startDestination = BottomNavItem.Demos.route,
        route = DEMO_GRAPH_ROUTE
    ) {
        composable(
            route = BottomNavItem.Demos.route
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