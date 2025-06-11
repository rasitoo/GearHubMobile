package com.example.gearhubmobile.ui.navigation



import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.gearhubmobile.MainScreen
import com.example.gearhubmobile.ui.screens.InitScreen
import com.example.gearhubmobile.ui.screens.Screen
import com.example.gearhubmobile.ui.screens.chat.ChatListScreen
import com.example.gearhubmobile.ui.screens.home.HomeScreen
import com.example.gearhubmobile.ui.screens.home.PlaceholderScreen
import com.example.gearhubmobile.ui.screens.login.AuthViewModel
import com.example.gearhubmobile.ui.screens.login.CreateUserScreen
import com.example.gearhubmobile.ui.screens.login.LoginScreen
import com.example.gearhubmobile.ui.screens.login.RecoverScreen
import com.example.gearhubmobile.ui.screens.login.RegisterScreen
import com.example.gearhubmobile.ui.screens.login.StartScreen
import com.example.gearhubmobile.ui.screens.message.ChatDetailScreen

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
@Composable
fun AppNavHost(navController: NavHostController,viewModel: AuthViewModel = hiltViewModel()) {
    NavHost(navController = navController, startDestination = InitScreen.Start.route) {
        composable(InitScreen.Start.route) {
            StartScreen(navController)
        }
        composable(InitScreen.Login.route) {
            LoginScreen(viewModel = viewModel, navController = navController) {
                navController.navigate(InitScreen.Start.route)
            }
        }
        composable(Screen.Home.route) {
            MainScreen()
        }
        composable(InitScreen.Register.route){
            RegisterScreen(
                viewModel,
                navController
            )
        }
        composable(InitScreen.Recover.route){
            RecoverScreen(
                viewModel,
                navController
            )
        }
        composable(InitScreen.CreateUser.route){
            CreateUserScreen(
                viewModel,
                navController
            )
        }
    }
}

@Composable
fun MainNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen()
        }
        composable(Screen.Communities.route) {
            PlaceholderScreen("Comunidades")
        }
        composable(route = Screen.CommunityDetail.route,
            arguments = listOf(navArgument("communityId") { type = NavType.StringType })
        ) { backStackEntry ->
            val communityId = backStackEntry.arguments?.getString("communityId") ?: return@composable
            ChatDetailScreen(chatId = communityId)
        }
        composable(Screen.Post.route) {
            PlaceholderScreen("Publicar")
        }
        composable(Screen.Chats.route) {
            ChatListScreen(onChatClick = { chatId ->
                navController.navigate(Screen.ChatDetail.createRoute(chatId.toString()))
            })
        }
        composable(route = Screen.ChatDetail.route,
            arguments = listOf(navArgument("chatId") { type = NavType.StringType })
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: return@composable
            ChatDetailScreen(chatId = chatId)
        }
    }
}

