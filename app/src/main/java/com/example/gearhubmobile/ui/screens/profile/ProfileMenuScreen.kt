package com.example.gearhubmobile.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.gearhubmobile.data.models.ResponseDTO
import com.example.gearhubmobile.data.models.Thread
import com.example.gearhubmobile.ui.components.HeartButton

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
@Composable
fun ProfileDetailScreen(
    userId: String?,
    viewModel: ProfileViewModel,
    navController: NavHostController
) {
    LaunchedEffect(Unit) {
        if (userId == "null") {
            viewModel.getTokenUser()
        } else
            viewModel.getUser(userId.toString())
    }
    val profile = viewModel.user
    var isThread = rememberSaveable { mutableStateOf(true) }
    val selectedTab = rememberSaveable { mutableIntStateOf(0) }

    val tabTitles = listOf(
        "Posts por Likes",
        "Posts por Creador",
        "Respuestas por Likes",
        "Respuestas por Creador"
    )

    val scrollState = rememberScrollState()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("vehicles")
            }) {
                Icon(Icons.Default.Build, contentDescription = "Mis coches")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = "http://vms.iesluisvives.org:25003" + profile?.profilePicture,
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (profile != null) {
                Text(profile.name, style = MaterialTheme.typography.headlineSmall)
                Text(profile.description, style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(24.dp))

            TabRow(selectedTabIndex = selectedTab.value) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab.value == index,
                        onClick = { selectedTab.value = index },
                        text = { Text(title, fontSize = 12.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LaunchedEffect(profile, selectedTab.intValue) {
                profile?.let {
                    when (selectedTab.intValue) {
                        0 -> {
                            isThread.value = true
                            viewModel.getPostsByLikes(it.userId)
                        }

                        1 -> {
                            isThread.value = true
                            viewModel.getPostsByCreator(it.userId)
                        }

                        2 -> {
                            isThread.value = false
                            viewModel.getResponsesByLikes(it.userId)
                        }

                        3 -> {
                            isThread.value = false
                            viewModel.getResponsesByCreator(it.userId)
                        }
                    }
                }
            }
            if (profile == null) {
                Text("Cargando perfil...", style = MaterialTheme.typography.bodyMedium)
            } else {
                if (isThread.value) {
                    if (viewModel.threads.isNullOrEmpty()) {
                        Text("No hay posts.", style = MaterialTheme.typography.bodyMedium)
                    } else {
                        PostList(viewModel.threads)
                    }
                } else {
                    if (viewModel.responses.isNullOrEmpty()) {
                        Text("No hay respuestas.", style = MaterialTheme.typography.bodyMedium)
                    } else {
                        ResponseList(viewModel.responses, viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun PostList(posts: List<Thread>?) {
    Column {
        posts?.forEach {
            Text(it.title, style = MaterialTheme.typography.titleMedium)
            Text(it.content, style = MaterialTheme.typography.bodySmall)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}

@Composable
fun ResponseList(responses: List<ResponseDTO>?, viewModel: ProfileViewModel) {
    var isLiked by rememberSaveable { mutableStateOf(false) }
    Column {
        responses?.forEach { it ->
            androidx.compose.material3.Card(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                Column(Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = "http://vms.iesluisvives.org:25003" + viewModel.responsesUsers.get(
                                it.creatorId
                            )?.profilePicture,
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            viewModel.responsesUsers.get(it.creatorId)?.userName ?: "Usuario",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }

                    Spacer(Modifier.height(8.dp))
                    Text(it.content ?: "", style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        isLiked = viewModel.likesState[it.id] == true
                        HeartButton(
                            isLiked = isLiked,
                            onToggle = {
                                if (isLiked) it.likes = it.likes!! - 1
                                else it.likes = it.likes!! + 1
                                viewModel.toggleLike(it.id)
                                isLiked = !isLiked
                            }
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            it.likes?.toString() ?: "0",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}


