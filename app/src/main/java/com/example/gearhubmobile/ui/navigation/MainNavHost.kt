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
import com.example.gearhubmobile.StartScreen
import com.example.gearhubmobile.ui.screens.chat.ChatListScreen
import com.example.gearhubmobile.ui.screens.chat.ChatViewModel
import com.example.gearhubmobile.ui.screens.chat.CreateChatScreen
import com.example.gearhubmobile.ui.screens.chat.SelectUsersScreen
import com.example.gearhubmobile.ui.screens.community.CommunityViewModel
import com.example.gearhubmobile.ui.screens.home.HomeScreen
import com.example.gearhubmobile.ui.screens.home.HomeViewModel
import com.example.gearhubmobile.ui.screens.home.PlaceholderScreen
import com.example.gearhubmobile.ui.screens.login.AuthViewModel
import com.example.gearhubmobile.ui.screens.login.CreateUserScreen
import com.example.gearhubmobile.ui.screens.login.LoginScreen
import com.example.gearhubmobile.ui.screens.login.RecoverScreen
import com.example.gearhubmobile.ui.screens.login.RegisterScreen
import com.example.gearhubmobile.ui.screens.message.ChatMessagesScreen
import com.example.gearhubmobile.ui.screens.message.MessageViewModel
import com.example.gearhubmobile.ui.screens.post.PostViewModel
import com.example.gearhubmobile.ui.screens.profile.ProfileDetailScreen
import com.example.gearhubmobile.ui.screens.profile.ProfileViewModel
import com.example.gearhubmobile.ui.screens.vehicle.AddVehicleScreen
import com.example.gearhubmobile.ui.screens.vehicle.VehicleViewModel
import com.example.gearhubmobile.ui.screens.vehicle.VehiclesScreen

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
@Composable
fun MainNavHost(navController: NavHostController, modifier: Modifier) {
    hiltViewModel<AuthViewModel>()
    val chatViewModel = hiltViewModel<ChatViewModel>()
    hiltViewModel<CommunityViewModel>()
    hiltViewModel<HomeViewModel>()
    val messageViewModel = hiltViewModel<MessageViewModel>()
    hiltViewModel<PostViewModel>()
    val profileViewModel = hiltViewModel<ProfileViewModel>()
    val vehicleViewModel = hiltViewModel<VehicleViewModel>()
    NavHost(
        navController = navController, startDestination = Routes.HOME, modifier = modifier
    ) {
        composable(Routes.COMMUNITIES) {
            PlaceholderScreen("Comunidades")
        }
        composable(Routes.HOME) {
            HomeScreen()
        }
        composable(
            route = Routes.COMMUNITY_DETAIL,
            arguments = listOf(navArgument("communityId") { type = NavType.StringType })
        ) { backStackEntry ->
            val communityId =
                backStackEntry.arguments?.getString("communityId") ?: return@composable
            ChatMessagesScreen(chatId = communityId, messageViewModel)
        }
        composable(Routes.POST) {
            PlaceholderScreen("Publicar")
        }

        composable(Routes.CREATE_CHAT) {
            CreateChatScreen(chatViewModel, navController = navController)
        }
        composable(
            route = Routes.VEHICLES, arguments = listOf(
            navArgument("userId") {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            }
        )) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            VehiclesScreen(userId, vehicleViewModel, navController = navController)
        }
        composable(Routes.ADD_VEHICLE) {
            AddVehicleScreen(vehicleViewModel, navController = navController)
        }
        composable(Routes.SELECT_USERS) {
            SelectUsersScreen(chatViewModel, navController = navController)
        }
        composable(
            route = Routes.USER_DETAIL,
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.StringType
                    defaultValue = null
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            ProfileDetailScreen(
                userId = userId.toString(),
                viewModel = profileViewModel,
                navController = navController
            )
        }
        composable(Routes.CHATS) {
            ChatListScreen(navController, onChatClick = { chatId ->
                navController.navigate("${Routes.CHAT_DETAIL_BASE}/$chatId")
            }, chatViewModel)
        }
        composable(
            route = Routes.CHAT_DETAIL,
            arguments = listOf(navArgument("chatId") { type = NavType.StringType })
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: "-1"
            ChatMessagesScreen(chatId = chatId, messageViewModel)
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController) {
    val authViewModel = hiltViewModel<AuthViewModel>()
    val communityViewModel = hiltViewModel<CommunityViewModel>()
    NavHost(navController = navController, startDestination = Routes.START) {
        composable(Routes.START) {
            StartScreen(navController, authViewModel)
        }
        composable(Routes.LOGIN) {
            LoginScreen(viewModel = authViewModel, navController = navController)
        }
        composable(Routes.MAIN) {
            MainScreen(communityViewModel)
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                authViewModel,
                navController
            )
        }
        composable(Routes.RECOVER) {
            RecoverScreen(
                authViewModel,
                navController
            )
        }
        composable(Routes.CREATE_USER) {
            CreateUserScreen(
                authViewModel,
                navController
            )
        }
    }
}
