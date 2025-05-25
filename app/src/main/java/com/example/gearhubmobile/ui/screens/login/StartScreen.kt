package com.example.gearhubmobile.ui.screens.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.gearhubmobile.ui.screens.InitScreen
import com.example.gearhubmobile.utils.SessionManager
import androidx.compose.runtime.getValue

/**
 * @author Rodrigo
 * @date 25 mayo, 2025
 */
@Composable
fun StartScreen(
    sessionManager: SessionManager,
    navController: NavHostController
) {
    val token by sessionManager.token.collectAsState(initial = null)

    LaunchedEffect(token) {
        if (token != null) {
            navController.navigate(InitScreen.Home.route) {
                popUpTo(InitScreen.Start.route) { inclusive = true }
            }
        } else {
            navController.navigate(InitScreen.Login.route) {
                popUpTo(InitScreen.Start.route) { inclusive = true }
            }
        }
    }

    // Muestra un loader mientras decide
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
