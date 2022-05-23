package com.example.jetpackapp.ui.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.snap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jetpackapp.navigation.Navigation
import com.example.jetpackapp.navigation.BottomNavItem
import com.example.jetpackapp.navigation.Screens
import com.example.jetpackapp.ui.theme.JetpackAppTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import java.util.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen() {
    val navController = rememberAnimatedNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarState = rememberSaveable {
        mutableStateOf(true)
    }

    when (currentRoute) {
        /*Screens.Character.route -> {
            bottomBarState.value = false
        }*/
        Screens.Test1.route -> {
            bottomBarState.value = false
        }
        else -> {
            bottomBarState.value = true
        }
    }

    JetpackAppTheme {
        Scaffold(
            bottomBar = {
                AnimatedVisibility(
                    visible = bottomBarState.value,
                    /*enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),*/
                ) {
                    BottomAppBar(
                        navController,
                        bottomBarState,
                    )
                }
            },
            content = {
                Navigation(navController = navController)
            }
        )
    }
}

@Composable
fun BottomAppBar(navController: NavController, bottomBarState: MutableState<Boolean>) {
    val items = listOf(
        BottomNavItem.Characters,
        BottomNavItem.Demos,
    )

    BottomNavigation(
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination

        items.forEach { item ->
            BottomNavigationItem(
                selected = currentRoute?.hierarchy?.any { it.route == item.Route } == true,
                label = {
                    Text(item.Route.replaceFirstChar {
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
                    navController.navigate(item.Route) {
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