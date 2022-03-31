package com.example.jetpackapp.ui.screens

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jetpackapp.navigation.Navigation
import com.example.jetpackapp.navigation.BottomNavItem
import com.example.jetpackapp.ui.theme.JetpackAppTheme

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    JetpackAppTheme {
        Scaffold(
            topBar = { TopBar(navController) },
            bottomBar = { BottomAppBar(navController) },
        ) {
            Navigation(navController = navController)
        }
    }
}

@Composable
fun TopBar(navController: NavController) {
    TopAppBar(
        title = {
            Text("Jetpack App")
        }
    )
}

@Composable
fun BottomAppBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Characters,
        BottomNavItem.Episodes,
    )
    BottomNavigation(
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            BottomNavigationItem(
                selected = currentRoute == item.Route,
                icon = {
                    Icon(
                        painterResource(id = item.icon),
                        contentDescription = item.title,
                    )
                },
                onClick = {
                    navController.navigate(item.Route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        // Avoid multiple copies of the same destination
                        launchSingleTop = true
                        // Restore state when re-selecting a previous route
                        restoreState = true
                    }
                },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MainScreen()
}