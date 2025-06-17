package com.example.gearhubmobile.ui.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.example.gearhubmobile.ui.screens.chat.EditChatScreen
import com.example.gearhubmobile.ui.screens.chat.SelectUsersScreen
import com.example.gearhubmobile.ui.screens.community.CommunityDetailScreen
import com.example.gearhubmobile.ui.screens.community.CommunityViewModel
import com.example.gearhubmobile.ui.screens.community.CreateCommunityScreen
import com.example.gearhubmobile.ui.screens.home.HomeScreen
import com.example.gearhubmobile.ui.screens.login.AuthViewModel
import com.example.gearhubmobile.ui.screens.login.CreateUserScreen
import com.example.gearhubmobile.ui.screens.login.LoginScreen
import com.example.gearhubmobile.ui.screens.login.LogoutScreen
import com.example.gearhubmobile.ui.screens.login.RecoverScreen
import com.example.gearhubmobile.ui.screens.login.RegisterScreen
import com.example.gearhubmobile.ui.screens.message.ChatInfoScreen
import com.example.gearhubmobile.ui.screens.message.ChatMessagesScreen
import com.example.gearhubmobile.ui.screens.message.MessageViewModel
import com.example.gearhubmobile.ui.screens.post.CreatePostScreen
import com.example.gearhubmobile.ui.screens.post.PostDetailScreen
import com.example.gearhubmobile.ui.screens.post.PostViewModel
import com.example.gearhubmobile.ui.screens.profile.ProfileDetailScreen
import com.example.gearhubmobile.ui.screens.profile.ProfileViewModel
import com.example.gearhubmobile.ui.screens.profile.UserListScreen
import com.example.gearhubmobile.ui.screens.review.AddReviewScreen
import com.example.gearhubmobile.ui.screens.review.ReviewViewModel
import com.example.gearhubmobile.ui.screens.review.ReviewsScreen
import com.example.gearhubmobile.ui.screens.vehicle.AddVehicleScreen
import com.example.gearhubmobile.ui.screens.vehicle.VehicleViewModel
import com.example.gearhubmobile.ui.screens.vehicle.VehiclesScreen

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
@Composable
fun MainNavHost(navController: NavHostController, modifier: Modifier) {
    val authViewModel = hiltViewModel<AuthViewModel>()
    val chatViewModel = hiltViewModel<ChatViewModel>()
    val communityViewModel = hiltViewModel<CommunityViewModel>()
    val messageViewModel = hiltViewModel<MessageViewModel>()
    val postViewModel = hiltViewModel<PostViewModel>()
    val profileViewModel = hiltViewModel<ProfileViewModel>()
    val vehicleViewModel = hiltViewModel<VehicleViewModel>()
    val reviewViewModel = hiltViewModel<ReviewViewModel>()
    NavHost(
        navController = navController, startDestination = Routes.HOME, modifier = modifier
    ) {
        composable(Routes.HOME) {
            HomeScreen(navController, communityViewModel)
        }

        composable(Routes.LOGOUT) {
            val context = LocalContext.current
            LogoutScreen(authViewModel, context)
        }
        composable(
            route = Routes.COMMUNITY_DETAIL,
            arguments = listOf(navArgument("communityId") { type = NavType.StringType })
        ) { backStackEntry ->
            val communityId =
                backStackEntry.arguments?.getString("communityId") ?: return@composable
            CommunityDetailScreen(
                communityId = communityId, viewModel = communityViewModel,
                navController = navController
            )
        }
        composable(Routes.USERS) {
            UserListScreen(
                viewModel = profileViewModel,
                onUserClick = { user ->
                    navController.navigate("${Routes.USER_DETAIL_BASE}/${user.userId}")
                }
            )
        }

        composable(Routes.CREATE_CHAT) {
            CreateChatScreen(chatViewModel, navController = navController)
        }
        composable(
            route = Routes.EDIT_CHAT, arguments = listOf(
                navArgument("chatId") {
                    type = NavType.StringType
                    defaultValue = null
                    nullable = true
                }
            )) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId")
            EditChatScreen(chatViewModel, navController = navController, chatId.toString())
        }
        composable(
            route = Routes.VEHICLES_DETAIL, arguments = listOf(
                navArgument("userId") {
                    type = NavType.StringType
                    defaultValue = null
                    nullable = true
                }
            )) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            VehiclesScreen(userId, vehicleViewModel, navController = navController)
        }
        composable(
            route = Routes.ADD_VEHICLE_EXTENDED, arguments = listOf(
                navArgument("vehicleId") {
                    type = NavType.StringType
                    defaultValue = null
                    nullable = true
                }
            )) { backStackEntry ->
            val vehicleId = backStackEntry.arguments?.getString("vehicleId")
            AddVehicleScreen(vehicleViewModel, navController = navController)
        }
        composable(Routes.ADD_VEHICLE) {
            AddVehicleScreen(vehicleViewModel, navController = navController)
        }
        composable(
            route = Routes.REVIEWS_DETAIL, arguments = listOf(
                navArgument("userId") {
                    type = NavType.StringType
                    defaultValue = null
                    nullable = true
                }
            )) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            ReviewsScreen(userId, reviewViewModel, navController = navController)
        }
        composable(Routes.ADD_REVIEW) {
            AddReviewScreen(reviewViewModel, navController = navController)
        }
        composable(Routes.SELECT_USERS) {
            SelectUsersScreen(chatViewModel, navController = navController)
        }
        composable(Routes.POST) {
            CreatePostScreen(viewModel = postViewModel, navController = navController)
        }
        composable(Routes.CREATE_COMMUNITY) {
            CreateCommunityScreen(viewModel = communityViewModel, navController = navController)
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
            ChatListScreen(
                navController,
                onChatClick = { chatId ->
                    navController.navigate("${Routes.CHAT_DETAIL_BASE}/$chatId")
                },
                onChatEdit = { id -> navController.navigate("${Routes.EDIT_CHAT_BASE}/$id") },
                chatViewModel
            )
        }
        composable(
            route = Routes.CHAT_DETAIL,
            arguments = listOf(navArgument("chatId") { type = NavType.StringType })
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: "-1"
            ChatMessagesScreen(
                chatId = chatId,
                navHostController = navController,
                viewModel = messageViewModel,
                onChatDetailClick = { id ->
                    navController.navigate(
                        Routes.CHAT_INFO_BASE
                    )
                })
        }
        composable(
            route = Routes.CHAT_INFO_BASE,
        ) {
            ChatInfoScreen(messageViewModel)
        }

        composable(
            route = Routes.POST_DETAIL,
            arguments = listOf(navArgument("threadId") { type = NavType.StringType })
        ) { backStackEntry ->
            val threadId = backStackEntry.arguments?.getString("threadId") ?: "-1"
            PostDetailScreen(threadId = threadId, postViewModel, communityViewModel)
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
