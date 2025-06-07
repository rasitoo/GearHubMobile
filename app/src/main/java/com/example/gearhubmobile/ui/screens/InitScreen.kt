package com.example.gearhubmobile.ui.screens

/**
 * @author Rodrigo
 * @date 25 mayo, 2025
 */
sealed class InitScreen(val route: String) {
    object Start : InitScreen("start")
    object Login : InitScreen("login")
    object Home : InitScreen("home")
    object Register : InitScreen("register")
    object Recover : InitScreen("recover")
    object CreateUser : InitScreen("create_user")
}
