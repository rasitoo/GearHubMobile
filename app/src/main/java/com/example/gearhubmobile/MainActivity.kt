package com.example.gearhubmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gearhubmobile.ui.screens.Screen
import com.example.gearhubmobile.ui.screens.community.CommunityList
import com.example.gearhubmobile.ui.screens.community.CommunityViewModel
import com.example.gearhubmobile.ui.screens.home.HomeScreen
import com.example.gearhubmobile.ui.screens.home.PlaceholderScreen
import com.example.gearhubmobile.ui.theme.GearHubMobileTheme
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.gearhubmobile.data.apirest.RetrofitInstance
import com.example.gearhubmobile.data.repositories.AuthRepository
import com.example.gearhubmobile.ui.screens.InitScreen
import com.example.gearhubmobile.ui.screens.login.AuthViewModel
import com.example.gearhubmobile.ui.screens.login.AuthViewModelFactory
import com.example.gearhubmobile.ui.screens.login.LoginScreen
import com.example.gearhubmobile.utils.SessionManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GearHubMobileTheme {
                val navController = rememberNavController()
                val context = LocalContext.current
                val sessionManager = remember { SessionManager(context) }

                AppNavHost(navController = navController, sessionManager = sessionManager)
            }
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController, sessionManager: SessionManager) {
    NavHost(navController = navController, startDestination = InitScreen.Start.route) {
        composable(InitScreen.Start.route) {
            StartScreen(sessionManager, navController)
        }
        composable(InitScreen.Login.route) {
            val repository = remember { AuthRepository(RetrofitInstance.authApi, sessionManager) }
            val factory = remember { AuthViewModelFactory(repository) }
            val viewModel: AuthViewModel = viewModel(factory = factory)
            LoginScreen(viewModel = viewModel) {
                navController.navigate(Screen.Home.route) {
                    popUpTo(InitScreen.Login.route) { inclusive = true }
                }
            }
        }
        composable(Screen.Home.route) {
            MainScreen(navController = navController)
        }
    }
}

@Composable
fun StartScreen(sessionManager: SessionManager, navController: NavHostController) {
    val token by sessionManager.token.collectAsState(initial = null)

    LaunchedEffect(token) {
        if (token != null) {
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
fun MainScreen(navController: NavHostController) {
    val communityViewModel: CommunityViewModel = viewModel()

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
                    onCommunityClick = { navController.navigate(Screen.Communities.route) }
                )
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
                            // Aquí podrías simular un drawer derecho como un `ModalBottomSheet` o `Dialog`
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
                            icon = { screen.icon?.let { Icon(it, contentDescription = screen.label ?: "") } },
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
                composable(Screen.Chats.route) { PlaceholderScreen("Chats") }
            }
        }
    }
}
