package com.example.gearhubmobile

import android.os.Bundle
import android.util.Base64
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.gearhubmobile.ui.navigation.AppNavHost
import com.example.gearhubmobile.ui.navigation.MainNavHost
import com.example.gearhubmobile.ui.screens.Screen
import com.example.gearhubmobile.ui.screens.community.CommunityList
import com.example.gearhubmobile.ui.screens.community.CommunityViewModel
import com.example.gearhubmobile.ui.theme.GearHubMobileTheme
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val communityViewModel: CommunityViewModel = hiltViewModel()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val bottomItems = listOf(
        Screen.Home, Screen.Communities, Screen.Post, Screen.Chats
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
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Gearhub") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate(Screen.UserDetail.route) }) {
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
                                screen.icon?.let { Icon(it, contentDescription = screen.label ?: "") }
                            },
                            label = { Text(screen.label ?: "") }
                        )
                    }
                }
            }
        ) { innerPadding ->
            MainNavHost(navController = navController, modifier = Modifier.padding(innerPadding))
        }
    }
}
