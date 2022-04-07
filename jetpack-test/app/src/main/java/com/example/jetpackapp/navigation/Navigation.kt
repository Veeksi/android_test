package com.example.jetpackapp.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import com.example.jetpackapp.ui.screens.CharacterListScreen
import com.example.jetpackapp.ui.screens.CharacterScreen
import com.example.jetpackapp.ui.screens.EpisodesScreen
import com.example.jetpackapp.ui.screens.TestScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation(navController: NavHostController, modifier: Modifier = Modifier) {
    AnimatedNavHost(navController, startDestination = BottomNavItem.Characters.Route, modifier = modifier) {
        composable(
            route = BottomNavItem.Characters.Route,
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -300 },
                    animationSpec = tween(
                        durationMillis = 600,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX  = { -300 },
                    animationSpec = tween(
                        durationMillis = 600,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(300))
            },

        ) {
            CharacterListScreen(
                navigateToCharacterDetails = {
                    navController.navigate(Screens.Character.passId(it))
                },
            )
        }
        composable(BottomNavItem.Episodes.Route) {
            EpisodesScreen(navController)
        }
        composable(
            Screens.Test.route,
        ) {
            TestScreen()
        }
        composable(
            route = Screens.Character.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                }
            ),
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { 300 },
                    animationSpec = tween(
                        durationMillis = 600,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(300))
            },
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX  = { 300 },
                    animationSpec = tween(
                        durationMillis = 600,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(300))
            },
        ) {
            CharacterScreen(it.arguments?.getString("id"))
        }
    }
}