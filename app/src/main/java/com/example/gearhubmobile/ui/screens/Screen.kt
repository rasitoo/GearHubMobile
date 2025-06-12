package com.example.gearhubmobile.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
sealed class Screen(val route: String, val icon: ImageVector?, val label: String?) {
    object Home : Screen("home", Icons.Default.Home, "Inicio")
    object Communities : Screen("communities", Icons.Default.Person, "Comunidades")
    object Post : Screen("post", Icons.Default.AddCircle, "Publicar")
    object UserDetail : Screen("user", null, null){
        fun createRoute(itemId: String) = "chatDetail/$itemId"
    }
    object Chats : Screen("chats", Icons.Default.Email, "Chats")
    object ChatDetail : Screen("chatDetail/{chatId}", null, null) {
        fun createRoute(itemId: String) = "chatDetail/$itemId"
    }
    object CommunityDetail : Screen("communityDetail/{communityId}", null, null) {
        fun createRoute(itemId: String) = "communityDetail/$itemId"
    }
}
