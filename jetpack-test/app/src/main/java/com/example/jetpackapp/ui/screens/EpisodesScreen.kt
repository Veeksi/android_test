package com.example.jetpackapp.ui.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jetpackapp.navigation.Screens

@Composable
fun EpisodesScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = {
                navController.navigate(Screens.Test.route) {
                    launchSingleTop = true
                }
            },
            modifier = Modifier.wrapContentSize(Alignment.Center)
        ) {
            Text(text = "Go to next page")
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Episodes page")
    }
}

@Preview
@Composable
fun PreviewEpisodesScreen() {
    EpisodesScreen(rememberNavController())
}