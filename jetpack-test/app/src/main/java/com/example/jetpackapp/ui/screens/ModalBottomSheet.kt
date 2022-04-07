package com.example.jetpackapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.jetpackapp.domain.model.Character


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModalBottomSheet(
    character: Character?,
    onView: () -> Unit,
    onDelete: () -> Unit,
) {
    Column {
        character?.let {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = character.image),
                    contentDescription = "Image",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Text(character.name, fontWeight = FontWeight.Bold)
            }
        }
        ListItem(
            modifier = Modifier.clickable {
                onView()
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = "View"
                )
            },
            text = { Text(text = "View") },
        )
        Divider()
        ListItem(
            modifier = Modifier.clickable {
                onDelete()
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete"
                )
            },
            text = { Text(text = "Delete") },
        )
    }
}