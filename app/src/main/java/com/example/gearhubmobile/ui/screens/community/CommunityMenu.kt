package com.example.gearhubmobile.ui.screens.community

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.gearhubmobile.data.models.CommunityDto

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
    viewModel: CommunityViewModel ,
    onCommunityClick: (CommunityDto) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.loadCommunities()
    }
    if (viewModel.isLoading) {
        CircularProgressIndicator(modifier = Modifier.padding(8.dp))
    } else {
        androidx.compose.foundation.lazy.LazyColumn {
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
                    ("http://vms.iesluisvives.org:25003/" + community.comPicture)
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
