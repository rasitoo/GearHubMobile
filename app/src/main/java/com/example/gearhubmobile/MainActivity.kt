package com.example.gearhubmobile

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gearhubmobile.ui.navigation.AppNavHost
import com.example.gearhubmobile.ui.navigation.MainNavHost
import com.example.gearhubmobile.ui.navigation.Routes
import com.example.gearhubmobile.ui.screens.Screen
import com.example.gearhubmobile.ui.screens.community.CommunityList
import com.example.gearhubmobile.ui.screens.community.CommunityViewModel
import com.example.gearhubmobile.ui.screens.login.AuthViewModel
import com.example.gearhubmobile.ui.theme.GearHubMobileTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GearHubMobileTheme {
                val navController = rememberNavController()
                AppNavHost(navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(communityViewModel: CommunityViewModel) {
    val navController = rememberNavController()

    val myCommunities by communityViewModel.myCommunities.collectAsState()
    val communities by communityViewModel.communities.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val bottomItems = listOf(
        Screen.Home, Screen.Users, Screen.Post, Screen.Chats
    )

    LaunchedEffect(Unit) {
        communityViewModel.getCurrentData()
        communityViewModel.loadCommunities()
    }
    ModalNavigationDrawer(

        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text("Menú comunidades", modifier = Modifier.padding(16.dp))
                    Divider()
                    Button(
                        onClick = {
                            navController.navigate(Routes.CREATE_COMMUNITY)
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Crear comunidad")
                        Text("Crear comunidad", modifier = Modifier.padding(start = 8.dp))
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Mis comunidades", modifier = Modifier.padding(16.dp))
                        CommunityList(
                            communities = myCommunities,
                            viewModel = communityViewModel,
                            onCommunityClick = { community ->
                                navController.navigate("${Routes.COMMUNITY_DETAIL_BASE}/${community.id}")
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.heightIn(max = 220.dp).fillMaxWidth()
                        )
                        Text("Recomendadas", modifier = Modifier.padding(16.dp))
                        CommunityList(
                            communities = communities,
                            viewModel = communityViewModel,
                            onCommunityClick = { community ->
                                navController.navigate("${Routes.COMMUNITY_DETAIL_BASE}/${community.id}")
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.fillMaxHeight().fillMaxWidth()
                        )
                    }
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Button(
                        onClick = {
                            navController.navigate(Routes.LOGOUT)
                        },
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text("Cerrar sesión")
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ), modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
                    title = {
                        Image(
                            painter = painterResource(id = R.drawable.logo_name),
                            contentDescription = "Logo Gearhub",
                            modifier = Modifier
                                .height(40.dp)
                                .width(95.dp),
                            contentScale = ContentScale.Crop
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate(Routes.USER_DETAIL_BASE + "/null") }) {
                            Icon(Icons.Default.AccountCircle, contentDescription = "Perfil")
                        }
                    }
                )
            },
            bottomBar = {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                NavigationBar {
                    bottomItems.forEach { screen ->
                        NavigationBarItem(
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            },
                            icon = {
                                Icon(
                                    screen.icon,
                                    contentDescription = screen.label
                                )
                            },
                            label = { Text(screen.label) }
                        )
                    }
                }
            }
        )
        { innerPadding ->
            MainNavHost(navController = navController, modifier = Modifier.padding(innerPadding))
        }
    }
}
@Composable
fun StartScreen(navController: NavHostController, viewModel: AuthViewModel) {
    var hasChecked by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(true) {
        if (!hasChecked) {
            hasChecked = true

            val token = viewModel.sessionManager.getCurrentToken()

            if (!token.isNullOrBlank()) {
                when (viewModel.checkUserStatus()) {
                    "OK" -> navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.START) { inclusive = true }
                    }

                    "NOT_FOUND" -> navController.navigate(Routes.CREATE_USER) {
                        popUpTo(Routes.START) { inclusive = true }
                    }

                    else -> {
                        viewModel.clearToken()
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.START) { inclusive = true }
                        }
                    }
                }
            } else {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.START) { inclusive = true }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
