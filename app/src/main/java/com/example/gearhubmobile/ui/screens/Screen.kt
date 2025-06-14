package com.example.gearhubmobile.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.gearhubmobile.ui.navigation.Routes

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
sealed class Screen(val route: String, val icon: ImageVector, val label: String) {
    object Home : Screen(Routes.HOME, Icons.Default.Home, "Inicio")
    object Communities : Screen(Routes.COMMUNITIES, Icons.Default.Person, "Comunidades")
    object Post : Screen(Routes.POST, Icons.Default.AddCircle, "Publicar")
    object Chats : Screen(Routes.CHATS, Icons.Default.Email, "Chats")
}
