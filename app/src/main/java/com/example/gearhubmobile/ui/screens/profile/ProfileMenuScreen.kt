package com.example.gearhubmobile.ui.screens.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.gearhubmobile.data.models.ResponseDTO
import com.example.gearhubmobile.data.models.Thread
import com.example.gearhubmobile.data.models.User
import com.example.gearhubmobile.ui.components.HeartButton
import com.example.gearhubmobile.ui.navigation.Routes

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
        } else {
            viewModel.getUser(userId.toString())
            viewModel.loadFollowers(userId.toString())
            viewModel.loadFollowing(userId.toString())
            viewModel.checkFollowStatus(userId.toString())
        }
    }

    val profile = viewModel.user
    val followers by viewModel.followers.collectAsState()
    val following by viewModel.following.collectAsState()
    val isFollowing = viewModel.isFollowing
    val currentUserId = viewModel.currentUserId

    var showUserList by rememberSaveable { mutableStateOf(false) }
    var showingFollowers by rememberSaveable { mutableStateOf(true) }

    if (showUserList) {
        UserListFollow(
            isFollowerList = showingFollowers,
            viewModel = viewModel,
            onUserClick = {
                showUserList = false
                navController.navigate("${Routes.USER_DETAIL_BASE}/${it.userId}")
            },
            onRemoveClick = { user ->
                if (!showingFollowers)
                    viewModel.toggleFollow(user.userId)
                else
                    viewModel.toggleFollowing(user.userId)
            }
        )

        return
    }

    val isThread = rememberSaveable { mutableStateOf(true) }
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
            if (profile?.type == 1) {
                FloatingActionButton(onClick = {
                    navController.navigate("${Routes.VEHICLES}/$userId")
                }) {
                    Icon(Icons.Default.Build, contentDescription = "Mis coches")
                }
            } else {
                FloatingActionButton(onClick = {
                    navController.navigate("${Routes.REVIEWS}/$userId")
                }) {
                    Icon(Icons.Default.Favorite, contentDescription = "Reseñas")
                }
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
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Seguidores: ${followers.size}",
                    modifier = Modifier.clickable {
                        showingFollowers = true
                        viewModel.setUsers(followers)
                        showUserList = true
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Seguidos: ${following.size}",
                    modifier = Modifier.clickable {
                        showingFollowers = false
                        viewModel.setUsers(following)
                        showUserList = true
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (profile?.userId != currentUserId) {
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = {

                    profile?.userId?.let { viewModel.toggleFollow(it) }
                }) {
                    Text(if (isFollowing) "Dejar de seguir" else "Seguir")
                }
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
                            viewModel.threads = emptyList()
                            viewModel.getPostsByLikes(it.userId)
                        }

                        1 -> {
                            isThread.value = true
                            viewModel.threads = emptyList()
                            viewModel.getPostsByCreator(it.userId)
                        }

                        2 -> {
                            isThread.value = false
                            viewModel.responses = emptyList()
                            viewModel.getResponsesByLikes(it.userId)
                        }

                        3 -> {
                            isThread.value = false
                            viewModel.responses = emptyList()
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
                        PostList(
                            viewModel.threads,
                            viewModel,
                            onPostClick = { post -> navController.navigate("${Routes.POST_DETAIL_BASE}/${post.id}") }
                        )
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
fun PostList(
    posts: List<Thread>?,
    viewModel: ProfileViewModel,
    onPostClick: (Thread) -> Unit = {}
) {
    Column {
        posts?.forEach { post ->
            androidx.compose.material3.Card(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
                    .clickable { onPostClick(post) }
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text(post.title, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    post.images.takeIf { it.isNotEmpty() }?.let { images ->
                        androidx.compose.foundation.lazy.LazyRow(modifier = Modifier.fillMaxWidth()) {
                            items(images.size) { idx ->
                                coil.compose.AsyncImage(
                                    model = "http://vms.iesluisvives.org:25003${images[idx]}",
                                    contentDescription = "Imagen del post",
                                    modifier = Modifier
                                        .size(120.dp)
                                        .padding(end = 8.dp)
                                        .clip(MaterialTheme.shapes.medium),
                                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val isLiked = viewModel.likesState[post.id] == true
                        HeartButton(
                            isLiked = isLiked,
                            onToggle = {
                                if (isLiked) post.likes = (post.likes ?: 1) - 1
                                else post.likes = (post.likes ?: 0) + 1
                                viewModel.toggleLike(post.id.toString())
                            },
                            activatedImageVector = Icons.Default.ThumbUp,
                            deactivatedImageVector = Icons.Outlined.ThumbUp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text((post.likes ?: 0).toString())
                    }
                }
            }
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
                            },
                            activatedImageVector = Icons.Default.Favorite,
                            deactivatedImageVector = Icons.Default.FavoriteBorder
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

@Composable
fun UserListScreen(
    viewModel: ProfileViewModel,
    onUserClick: (User) -> Unit = {}
) {
    LaunchedEffect(Unit) {
        viewModel.getUsers()
    }
    var searchText by remember { mutableStateOf("") }
    val users by viewModel.users.collectAsState()
    val filteredUsers = users.filter {
        it.userName.contains(searchText, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Buscar usuario") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        androidx.compose.foundation.lazy.LazyColumn {
            filteredUsers.forEach { user ->
                item {
                    androidx.compose.material3.Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { onUserClick(user) }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            coil.compose.AsyncImage(
                                model = "http://vms.iesluisvives.org:25003" + user.profilePicture,
                                contentDescription = "Foto de perfil",
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    user.userName,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    if (user.type == 2) "Taller" else "Usuario",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserListFollow(
    viewModel: ProfileViewModel,
    isFollowerList: Boolean,
    onUserClick: (User) -> Unit = {},
    onRemoveClick: (User) -> Unit = {}
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        val users by viewModel.users.collectAsState()
        Spacer(modifier = Modifier.height(16.dp))
        androidx.compose.foundation.lazy.LazyColumn {
            users.forEach { user ->
                item {
                    androidx.compose.material3.Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { onUserClick(user) }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            coil.compose.AsyncImage(
                                model = "http://vms.iesluisvives.org:25003" + user.profilePicture,
                                contentDescription = "Foto de perfil",
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    user.userName,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    if (user.type == 2) "Taller" else "Usuario",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                    )
                                )
                            }
                            if (viewModel.user?.id.toString() == viewModel.currentUserId.toString())
                                Button(
                                    onClick = { onRemoveClick(user) }
                                ) {
                                    Text(
                                        if (isFollowerList) "Eliminar seguidor" else "Dejar de seguir"
                                    )
                                }
                        }
                    }
                }
            }
        }
    }
}