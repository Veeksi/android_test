package com.example.jetpackapp.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.example.jetpackapp.ui.screens.CharacterScreen
import com.example.jetpackapp.ui.screens.CharactersListScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.charactersGraph(navController: NavController) {
    navigation(
        startDestination = BottomNavItem.Characters.route,
        route = CHARACTERS_GRAPH_ROUTE
    ) {
        composable(
            route = BottomNavItem.Characters.route,
        ) {
            CharactersListScreen(
                navigateToCharacterDetails = {
                    navController.navigate(Screens.Character.passId(it))
                },
            )
        }
        composable(
            route = Screens.Character.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                }
            ),
        ) {
            CharacterScreen(it.arguments?.getString("id"))
        }
    }
}