package com.example.gearhubmobile

import android.os.Bundle
import android.util.Base64
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gearhubmobile.ui.screens.InitScreen
import com.example.gearhubmobile.ui.screens.Screen
import com.example.gearhubmobile.ui.screens.chat.ChatListScreen
import com.example.gearhubmobile.ui.screens.community.CommunityList
import com.example.gearhubmobile.ui.screens.community.CommunityViewModel
import com.example.gearhubmobile.ui.screens.home.HomeScreen
import com.example.gearhubmobile.ui.screens.home.PlaceholderScreen
import com.example.gearhubmobile.ui.screens.login.AuthViewModel
import com.example.gearhubmobile.ui.screens.login.LoginScreen
import com.example.gearhubmobile.ui.screens.message.ChatDetailScreen
import com.example.gearhubmobile.ui.screens.message.MessageViewModel
import com.example.gearhubmobile.ui.theme.GearHubMobileTheme
import com.example.gearhubmobile.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.json.JSONObject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GearHubMobileTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController)
            }
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = InitScreen.Start.route) {

        composable(InitScreen.Start.route) {
            val sessionManager: SessionManager = hiltViewModel<AuthViewModel>().sessionManager
            StartScreen(sessionManager, navController)
        }

        composable(InitScreen.Login.route) {
            val authViewModel: AuthViewModel = hiltViewModel()
            LoginScreen(viewModel = authViewModel) {
                navController.navigate(Screen.Home.route) {
                    popUpTo(InitScreen.Login.route) { inclusive = true }
                }
            }
        }

        composable(Screen.Home.route) {
            MainScreen()
        }
    }
}

@Composable
fun StartScreen(sessionManager: SessionManager, navController: NavHostController, viewModel: AuthViewModel = hiltViewModel()
) {
    val token by sessionManager.token.collectAsState(initial = null)

    LaunchedEffect(token) {
        if (!token.isNullOrBlank() && !viewModel.isTokenExpired(token)) {
            navController.navigate(Screen.Home.route) {
                popUpTo(InitScreen.Start.route) { inclusive = true }
            }
        } else {
            navController.navigate(InitScreen.Login.route) {
                popUpTo(InitScreen.Start.route) { inclusive = true }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val communityViewModel: CommunityViewModel = hiltViewModel()

    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val bottomItems = listOf(
        Screen.Home,
        Screen.Communities,
        Screen.Post,
        Screen.Chats
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Menú comunidades", modifier = Modifier.padding(16.dp))
                Divider()
                Text("Mis comunidades", modifier = Modifier.padding(16.dp))
                Text("Recomendadas", modifier = Modifier.padding(16.dp))
                CommunityList(
                    viewModel = communityViewModel,
                    onCommunityClick = { community ->
                        navController.navigate(Screen.CommunityDetail.createRoute(community.id))
                    })
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Gearhub") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            // Drawer derecho o navegacion a perfilApi
                        }) {
                            Icon(Icons.Default.AccountCircle, contentDescription = "Perfil")
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    bottomItems.forEach { screen ->
                        NavigationBarItem(
                            selected = navController.currentDestination?.route == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            },
                            icon = {
                                screen.icon?.let {
                                    Icon(
                                        it,
                                        contentDescription = screen.label ?: ""
                                    )
                                }
                            },
                            label = { Text(screen.label ?: "") }
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Home.route) { HomeScreen() }
                composable(Screen.Communities.route) { PlaceholderScreen("Comunidades") }
                composable(Screen.Post.route) { PlaceholderScreen("Publicar") }
                composable(Screen.Chats.route) {
                    ChatListScreen(onChatClick = { chatId ->
                        navController.navigate(Screen.ChatDetail.createRoute(chatId.toString()))
                    })
                }
                composable(
                    route = Screen.ChatDetail.route,
                    arguments = listOf(navArgument("chatId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val chatId = backStackEntry.arguments?.getString("chatId") ?: return@composable
                    ChatDetailScreen(chatId = chatId)
                }
            }
        }
    }
}
