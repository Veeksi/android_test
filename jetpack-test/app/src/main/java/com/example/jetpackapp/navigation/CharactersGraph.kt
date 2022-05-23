package com.example.jetpackapp.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.*
import com.example.jetpackapp.ui.screens.CharacterScreen
import com.example.jetpackapp.ui.screens.CharactersListScreen
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.charactersGraph(navController: NavHostController) {
    navigation(
        startDestination = BottomNavItem.Characters.Route,
        route = CHARACTERS_GRAPH_ROUTE
    ) {
        composable(
            route = BottomNavItem.Characters.Route,
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