package com.example.jetpackapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EpisodesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = {

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
    EpisodesScreen()
}