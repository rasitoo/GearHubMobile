package com.example.gearhubmobile.ui.screens.community

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.gearhubmobile.data.models.Community
import com.example.gearhubmobile.data.models.CommunityDto
import com.example.gearhubmobile.data.models.Thread
import com.example.gearhubmobile.ui.components.HeartButton
import com.example.gearhubmobile.ui.navigation.Routes

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
@Composable
fun AllCommunities(viewModel: CommunityViewModel) {
    LaunchedEffect(Unit) {
        viewModel.loadCommunities()
    }
    if (viewModel.isLoading) {
        CircularProgressIndicator(modifier = Modifier.padding(8.dp))
    } else {
        viewModel.communities.forEach { community ->
            Text(
                text = community.comName,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}


@Composable
fun CommunityList(
    communities: List<Community>,
    viewModel: CommunityViewModel,
    onCommunityClick: (Community) -> Unit,
    modifier: Modifier = Modifier

) {

    if (viewModel.isLoading) {
        CircularProgressIndicator(modifier = Modifier.padding(8.dp))
    } else {
        LazyColumn(modifier = modifier) {
            items(communities.size) { index ->
                val community = communities[index]
                CommunityListItem(community = community, onClick = { onCommunityClick(community) })
            }
        }
    }
}

@Composable
fun CommunityListItem(
    community: Community,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    ("http://vms.iesluisvives.org:25003" + community.comPicture)
                ),
                contentDescription = "Imagen comunidad",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = community.comName,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityDetailScreen(
    communityId: String,
    navController: NavHostController,
    viewModel: CommunityViewModel
) {
    val community by viewModel.community
    val posts by viewModel.communityPosts
    val isLoading by viewModel::isLoading

    LaunchedEffect(Unit) {
        viewModel.loadCommunity(communityId)
        viewModel.loadPosts(communityId)
    }
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn {
            item {
                CommunityHeader(community)
            }

            posts.forEach { post ->
                item {
                    CommunityPostItem(
                        post,
                        onClick = {
                            navController.navigate("${Routes.POST_DETAIL_BASE}/${post.id}")},
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}


@Composable
fun CommunityHeader(community: Community?) {
    if (community == null) return

    Column(modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            model = "http://vms.iesluisvives.org:25003${community.comBanner}",
            contentDescription = "Banner",
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            contentScale = ContentScale.Crop
        )

        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "http://vms.iesluisvives.org:25003${community.comPicture}",
                contentDescription = "Imagen de perfil",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(community.comName, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text(community.comDescription ?: "", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun CommunityPostItem(
    post: Thread,
    onClick: () -> Unit,
    viewModel: CommunityViewModel
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        var isLiked by rememberSaveable { mutableStateOf(viewModel.likesState[post.id] == true) }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(post.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            post.images.takeIf { it.isNotEmpty() }?.let { images ->
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    images.forEach { imageUrl ->
                        item {
                            AsyncImage(
                                model = "http://vms.iesluisvives.org:25003$imageUrl",
                                contentDescription = "Imagen del post",
                                modifier = Modifier
                                    .size(120.dp)
                                    .padding(end = 8.dp)
                                    .clip(MaterialTheme.shapes.medium),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                HeartButton(
                    isLiked = isLiked,
                    onToggle = {
                        viewModel.toggleLike(post.id.toString())
                        if (isLiked) {
                            post.likes = (post.likes ?: 1) - 1
                        } else {
                            post.likes = (post.likes ?: 0) + 1
                        }
                        isLiked = !isLiked
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCommunityScreen(
    viewModel: CommunityViewModel,
    navController: NavHostController
) {
    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var profileImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var bannerImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var isLoading by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current

    val profileImagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> profileImageUri = uri }

    val bannerImagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> bannerImageUri = uri }

    if (viewModel.comCreated) {
        LaunchedEffect(Unit) {
            navController.popBackStack()
            viewModel.loadCommunities()
            viewModel.comCreated = false
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre de la comunidad") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 4
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Imagen de perfil")
                IconButton(onClick = { profileImagePicker.launch("image/*") }) {
                    Icon(
                        Icons.Default.AddCircle,
                        contentDescription = "Seleccionar imagen de perfil"
                    )
                }
                profileImageUri?.let { uri ->
                    AsyncImage(
                        model = uri,
                        contentDescription = "Imagen de perfil seleccionada",
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                    )
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Banner")
                IconButton(onClick = { bannerImagePicker.launch("image/*") }) {
                    Icon(Icons.Default.AddCircle, contentDescription = "Seleccionar banner")
                }
                bannerImageUri?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = "Banner seleccionado",
                        modifier = Modifier
                            .size(64.dp)
                            .clip(MaterialTheme.shapes.medium)
                    )
                }}}
        Spacer(modifier = Modifier.height(24.dp))
        viewModel.errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }
        Button(
            onClick = {
                if (name.isBlank() || description.isBlank()) {
                    viewModel.errorMessage = "Rellena todos los campos"
                    return@Button
                }
                isLoading = true
                viewModel.createCommunity(
                    name,
                    description,
                    profileImageUri,
                    bannerImageUri,
                    context
                )
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear comunidad")
        }
        if (isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        }
    }
}
