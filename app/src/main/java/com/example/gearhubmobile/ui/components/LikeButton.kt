package com.example.gearhubmobile.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * @author Rodrigo
 * @date 12 junio, 2025
 */


@Composable
fun HeartButton(
    isLiked: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    activatedImageVector: ImageVector,
    deactivatedImageVector: ImageVector,

    ) {
    IconButton(
        onClick = onToggle,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (isLiked) activatedImageVector else deactivatedImageVector,
            contentDescription = "Like",
            tint = if (activatedImageVector == Icons.Default.Favorite) Color.Red else Color(0xFFFFCB4C)
        )
    }
}