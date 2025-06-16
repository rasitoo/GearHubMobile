package com.example.gearhubmobile.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.gearhubmobile.data.models.Community
import com.example.gearhubmobile.data.models.CommunityDto
import com.example.gearhubmobile.data.models.Thread
import com.example.gearhubmobile.ui.navigation.Routes
import com.example.gearhubmobile.ui.screens.community.CommunityPostItem
import com.example.gearhubmobile.ui.screens.community.CommunityViewModel

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
@Composable
fun HomeScreen(
    navController: NavHostController,
    communitiesViewModel: CommunityViewModel
) {
    val communities by remember { derivedStateOf { communitiesViewModel.communities } }
    val threads by remember { derivedStateOf { communitiesViewModel.communityPosts } }
    val isLoading = communitiesViewModel.isLoading


    LaunchedEffect(Unit) {
        communitiesViewModel.loadAllPosts()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        item {
            Text("Comunidades", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {
                items(communities.size) { index ->
                    CommunityCardHorizontal(communities[index]) {
                        navController.navigate("${Routes.COMMUNITY_DETAIL_BASE}/${communities[index].id}")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Últimas publicaciones", style = MaterialTheme.typography.headlineSmall)
        }

        items(threads.value.size) { index ->
            CommunityPostItem(
                post = threads.value[index],
                onClick = {
                    navController.navigate("${Routes.POST_DETAIL_BASE}/${threads.value[index].id}")
                },
                viewModel = communitiesViewModel
            )
        }
    }

    if (isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun CommunityCardHorizontal(
    community: Community,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(start = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter("http://vms.iesluisvives.org:25003${community.comPicture}"),
                contentDescription = "Imagen comunidad",
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp)
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = community.comName,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1
            )
        }
    }
}


