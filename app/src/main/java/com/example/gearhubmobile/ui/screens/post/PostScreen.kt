package com.example.gearhubmobile.ui.screens.post

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.gearhubmobile.data.models.ResponseDTO
import com.example.gearhubmobile.ui.components.HeartButton
import com.example.gearhubmobile.ui.screens.community.CommunityHeader
import com.example.gearhubmobile.ui.screens.community.CommunityViewModel

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    navController: NavHostController,
    viewModel: PostViewModel
) {
    val context = LocalContext.current

    var expanded by remember { mutableStateOf(false) }
    var title by rememberSaveable { mutableStateOf("") }
    var message by rememberSaveable { mutableStateOf("") }
    var imageUris by rememberSaveable { mutableStateOf<List<Uri>>(emptyList()) }

    val communities = viewModel.communities.collectAsState()

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris -> if (uris != null) imageUris = uris }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Comunidad", style = MaterialTheme.typography.labelLarge)
        Box {
            OutlinedTextField(
                value = viewModel.selectedCommunity?.comName ?: "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Selecciona una comunidad") },
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Expandir")
                    }
                },
                readOnly = true
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                communities.value.forEach { community ->
                    DropdownMenuItem(
                        text = { Text(community.comName) },
                        onClick = {
                            viewModel.selectedCommunity = community
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("Mensaje") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            maxLines = 6
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Imágenes seleccionadas: ${imageUris.size}")

        LazyRow {
            items(imageUris.size) { index ->
                AsyncImage(
                    model = imageUris[index],
                    contentDescription = "Imagen seleccionada",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = {
                imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.AddCircle, contentDescription = "Añadir imágenes")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Seleccionar imágenes")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.selectedCommunity?.let { community ->
                    if (title.isNotBlank() && message.isNotBlank()) {
                        viewModel.createPost(
                            communityId = community.id,
                            title = title,
                            content = message,
                            imageUris = imageUris,
                            context = context
                        )
                        navController.popBackStack()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = viewModel.selectedCommunity != null
        ) {
            Text("Publicar")
        }
    }
}


@Composable
fun PostDetailScreen(
    threadId: String?,
    viewModel: PostViewModel,
    comViewModel: CommunityViewModel
) {
    LaunchedEffect(threadId) {
        viewModel.loadThread(threadId.toString())
    }
    val thread = viewModel.thread
    var showReplyBox by remember { mutableStateOf(false) }
    var replyText by remember { mutableStateOf("") }


    if (thread == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
        ) {
            CommunityHeader(viewModel.community.value, comViewModel)
            Card(
                Modifier.padding(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Text(thread.title, style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(8.dp))
                Text(thread.content, style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(8.dp))
                thread.images.takeIf { it.isNotEmpty() }?.let { images ->
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
            }

            Spacer(Modifier.height(16.dp))
            Divider()
            Spacer(Modifier.height(8.dp))
            Card(
                Modifier.padding(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                TextButton(onClick = { showReplyBox = !showReplyBox }) {
                    Text("Responder al hilo")
                }
                if (showReplyBox) {
                    Column {
                        OutlinedTextField(
                            value = replyText,
                            onValueChange = { replyText = it },
                            label = { Text("Escribe tu respuesta...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                        )
                        Button(
                            onClick = {
                                viewModel.createResponse(
                                    threadId = thread.id,
                                    parentId = "0",
                                    content = replyText
                                )
                                replyText = ""
                                showReplyBox = false
                            },
                            enabled = replyText.isNotBlank()
                        ) {
                            Text("Enviar")
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text("Respuestas", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                val responses =
                    viewModel.responsesByThread.value[thread.id] ?: emptyList<ResponseDTO>()
                if (responses.isEmpty()) {
                    Text(
                        "Aún no hay respuestas.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                } else {
                    responses.forEach { response ->
                        ResponseItem(
                            response = response,
                            indent = 0,
                            viewModel = viewModel,
                            threadID = thread.id
                        )
                        Divider(Modifier.padding(vertical = 4.dp))
                    }
                }
            }
        }
    }
}


@Composable
fun ResponseItem(response: ResponseDTO, indent: Int, viewModel: PostViewModel, threadID: String) {
    var showReplyBox by remember { mutableStateOf(false) }
    var replyText by remember { mutableStateOf("") }
    var isLiked by rememberSaveable { mutableStateOf(viewModel.likesState[response.id] == true) }


    Card(
        modifier = Modifier
            .padding(start = (indent * 16).dp, bottom = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(15.dp)
    ) {
        Column(Modifier.padding(12.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = "http://vms.iesluisvives.org:25003" + viewModel.responsesUsers[response.creatorId]?.profilePicture,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    viewModel.responsesUsers[response.creatorId]?.userName ?: "Usuario",
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Spacer(Modifier.height(2.dp))
            Text(response.content.toString(), style = MaterialTheme.typography.bodyMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                HeartButton(
                    isLiked = isLiked,
                    onToggle = {
                        if (isLiked) response.likes = (response.likes ?: 1) - 1
                        else response.likes = (response.likes ?: 0) + 1
                        viewModel.toggleResponseLike(response.id)
                        isLiked = !isLiked
                    },
                    activatedImageVector = Icons.Default.Favorite,
                    deactivatedImageVector = Icons.Default.FavoriteBorder
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    response.likes?.toString() ?: "0",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            TextButton(onClick = { showReplyBox = !showReplyBox }) {
                Text("Responder")
            }
            if (showReplyBox) {
                Column {
                    OutlinedTextField(
                        value = replyText,
                        onValueChange = { replyText = it },
                        label = { Text("Escribe tu respuesta...") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            viewModel.createResponse(
                                threadId = threadID,
                                parentId = response.id,
                                content = replyText
                            )
                            replyText = ""
                            showReplyBox = false
                        },
                        enabled = replyText.isNotBlank()
                    ) {
                        Text("Enviar")
                    }
                }
            }
            val childResponses = viewModel.responsesByResponse.value[response.id] ?: emptyList()
            if (childResponses.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                childResponses.forEach {
                    ResponseItem(
                        response = it,
                        indent = indent + 1,
                        viewModel = viewModel,
                        threadID
                    )
                }
            }
        }
    }
}