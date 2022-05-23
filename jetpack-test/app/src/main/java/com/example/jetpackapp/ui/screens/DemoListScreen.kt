package com.example.jetpackapp.ui.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jetpackapp.R
import com.example.jetpackapp.navigation.Screens

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DemoListScreen(navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 54.dp)
            .wrapContentSize(Alignment.TopStart),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(12 ) {index ->
            ListItem(
                text = { Text("Example title $index") },
                secondaryText = { Text("Example description") },
                icon = {
                    Icon(
                        painterResource(id = R.drawable.ic_home),
                        contentDescription = "Example",
                    )
                }
            )
        }
        /*Button(
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
        Text(text = "Episodes page")*/
    }
}

@Preview
@Composable
fun PreviewDemoListScreen() {
    DemoListScreen(rememberNavController())
}