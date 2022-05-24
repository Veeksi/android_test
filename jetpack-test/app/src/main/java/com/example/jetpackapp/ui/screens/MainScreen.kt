package com.example.jetpackapp.ui.screens

import androidx.compose.animation.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jetpackapp.navigation.*
import com.example.jetpackapp.ui.theme.JetpackAppTheme
import java.util.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val bottomBarState = rememberSaveable {
        mutableStateOf(true)
    }

    JetpackAppTheme {
        Scaffold(
            bottomBar = {
                AnimatedVisibility(
                    visible = bottomBarState.value,
                ) {
                    BottomAppBar(navController)
                }
            },
        ) {
            Navigation(navController = navController)
        }
    }
}

@Composable
fun BottomAppBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Characters,
        BottomNavItem.Demos,
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomNavigation(
        contentColor = Color.White
    ) {
        items.forEach { item ->
            BottomNavigationItem(
                selected = currentRoute == item.route, /*currentRoute?.hierarchy?.any { it.route == item.Route } == true,*/
                label = {
                    Text(item.route.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.ROOT
                        ) else it.toString()
                    })
                },
                icon = {
                    Icon(
                        painterResource(id = item.icon),
                        contentDescription = item.title,
                    )
                },
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
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