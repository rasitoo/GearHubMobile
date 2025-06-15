package com.example.gearhubmobile.ui.screens.community

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
    viewModel: CommunityViewModel,
    onCommunityClick: (CommunityDto) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.loadCommunities()
    }
    if (viewModel.isLoading) {
        CircularProgressIndicator(modifier = Modifier.padding(8.dp))
    } else {
        LazyColumn {
            items(viewModel.communities.size) { index ->
                val community = viewModel.communities[index]
                CommunityListItem(community = community, onClick = { onCommunityClick(community) })
            }
        }
    }
}

@Composable
fun CommunityListItem(
    community: CommunityDto,
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
                            navController.navigate(Routes.POST_DETAIL)
                        },
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
    post: Thread?,
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
        var isLiked by rememberSaveable { mutableStateOf(false) }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(post?.title ?: "Unknown", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            post?.images?.takeIf { it.isNotEmpty() }?.let { images ->
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
                isLiked = viewModel.likesState[post?.id ?: "0"] == true
                HeartButton(
                    isLiked = isLiked,
                    onToggle = {
                        if (isLiked) post?.likes = post.likes!! - 1
                        else post?.likes = post.likes!! + 1
                        viewModel.toggleLike(post?.id.toString())
                        isLiked = !isLiked
                    },
                    activatedImageVector = Icons.Default.ThumbUp,
                    deactivatedImageVector = Icons.Outlined.ThumbUp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text((post?.likes ?: 0).toString())
            }
        }
    }
}
