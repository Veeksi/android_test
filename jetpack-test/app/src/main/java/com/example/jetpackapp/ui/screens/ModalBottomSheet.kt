package com.example.jetpackapp.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class BottomSheetItem(val title: String, val icon: ImageVector)

val bottomSheetItems = listOf(
    BottomSheetItem(title = "View", icon = Icons.Default.Face),
    BottomSheetItem(title = "Delete", icon = Icons.Default.Delete)
)

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun BottomSheet() {
    ModalBottomSheetLayout(
        sheetContent = {
        Column(
            content = {
                Spacer(modifier = Modifier.padding(16.dp))
                Text(
                    text = "Create New",
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 21.sp,
                    color = Color.White
                )
                LazyVerticalGrid(
                    cells = GridCells.Fixed(3)
                ) {
                    items(bottomSheetItems.size, itemContent = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp)
                        ) {
                            Spacer(modifier = Modifier.padding(8.dp))
                            Icon(
                                bottomSheetItems[it].icon,
                                "",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.padding(8.dp))
                            Text(text = bottomSheetItems[it].title, color = Color.White)
                        }

                    })
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(Color(0xAA3fa7cc))
                .padding(16.dp)
        )
    }) {

    }
}