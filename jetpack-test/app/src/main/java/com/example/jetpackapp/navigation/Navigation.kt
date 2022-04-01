package com.example.jetpackapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.jetpackapp.ui.screens.CharactersScreen
import com.example.jetpackapp.ui.screens.EpisodesScreen
import com.example.jetpackapp.ui.screens.TestScreen
import com.example.jetpackapp.ui.vm.CharacterViewModel

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavItem.Characters.Route) {
        composable(BottomNavItem.Characters.Route) {
            CharactersScreen()
        }
        composable(BottomNavItem.Episodes.Route) {
            EpisodesScreen(navController)
        }
        composable(
            route = "TestScreen",
        ) {
            TestScreen()
        }
    }
}