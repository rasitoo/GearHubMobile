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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
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
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val allCommunities by communitiesViewModel.allCommunities.collectAsState()
    val allThreads by remember { derivedStateOf { communitiesViewModel.communityPosts } }
    val isLoading = communitiesViewModel.isLoading

    val filteredCommunities by remember(searchQuery, allCommunities) {
        derivedStateOf {
            allCommunities.filter {
                it.comName.contains(searchQuery, ignoreCase = true)
            }
        }
    }
    val filteredThreads by remember(searchQuery, allThreads) {
        derivedStateOf {
            allThreads.value.filter {
                it.title.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    LaunchedEffect(Unit) {
        communitiesViewModel.loadAllPosts()
        communitiesViewModel.loadCommunities()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Buscar comunidades o publicaciones") },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Text("Comunidades", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {
                items(filteredCommunities.size) { index ->
                    CommunityCardHorizontal(filteredCommunities[index]) {
                        navController.navigate("${Routes.COMMUNITY_DETAIL_BASE}/${filteredCommunities[index].id}")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Últimas publicaciones", style = MaterialTheme.typography.headlineSmall)
        }

        items(filteredThreads.size) { index ->
            CommunityPostItem(
                post = filteredThreads[index],
                onClick = {
                    navController.navigate("${Routes.POST_DETAIL_BASE}/${filteredThreads[index].id}")
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


