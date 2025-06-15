package com.example.gearhubmobile.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gearhubmobile.data.models.Community
import com.example.gearhubmobile.data.models.CommunityDto
import com.example.gearhubmobile.data.models.Thread

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel
) {
//    LaunchedEffect(Unit) {
//        viewModel.loadThreads()
//        viewModel.loadCommunities()
//    }
//    Column(modifier = Modifier.fillMaxSize()) {
//        androidx.compose.foundation.lazy.LazyRow(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 8.dp)
//        ) {
//            items(viewModel.communities.size) { index ->
//                val community = viewModel.communities[index]
//                androidx.compose.material3.Card(
//                    modifier = Modifier
//                        .padding(horizontal = 8.dp)
//                ) {
//                    Text(
//                        community.comName,
//                        modifier = Modifier.padding(16.dp),
//                        style = MaterialTheme.typography.titleSmall
//                    )
//                }
//            }
//        }
//        Spacer(modifier = Modifier.height(8.dp))
//        androidx.compose.foundation.lazy.LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = 8.dp)
//        ) {
//            items(viewModel.threads.size) { index ->
//                val thread = viewModel.threads[index]
//                androidx.compose.material3.Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 4.dp)
//                ) {
//                    Column(Modifier.padding(12.dp)) {
//                        Text(thread.title, style = MaterialTheme.typography.titleMedium)
//                    }
//                }
//            }
//        }
//    }
}

