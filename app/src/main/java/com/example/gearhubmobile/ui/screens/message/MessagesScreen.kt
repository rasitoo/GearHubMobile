package com.example.gearhubmobile.ui.screens.message

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.gearhubmobile.data.models.Message
import com.example.gearhubmobile.ui.navigation.Routes

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatMessagesScreen(
    chatId: String,
    viewModel: MessageViewModel,
    navHostController: NavHostController,
    onChatDetailClick: (String) -> Unit
) {
    val messages by viewModel.messages.collectAsState()
    val currentUserId by viewModel.currentUserId.collectAsState()
    val chat by viewModel.chat.collectAsState()

    val navigateToChatList by viewModel.navigateToChatList.collectAsState()
    if (navigateToChatList) {
        navHostController.navigate(Routes.CHATS)
    }
    LaunchedEffect(Unit) {
        viewModel.connectToChat(chatId.toInt())
        viewModel.loadChat(chatId)
        viewModel.loadUsers(chatId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        chat?.name.toString(),
                        modifier = Modifier
                            .clickable { onChatDetailClick(chatId) }
                    )
                },
                actions = {
                    IconButton(onClick = { onChatDetailClick(chatId) }) {
                        Icon(Icons.Default.Info, contentDescription = "Detalles del chat")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                reverseLayout = true
            ) {
                items(messages.reversed().size) { index ->
                    val message = messages.reversed()[index]
                    MessageBubble(
                        message,
                        viewModel = viewModel,
                        onDelete = { viewModel.deleteMessage(message.id.toString()) },
                        currentUserId = currentUserId.toString()
                    )
                }
            }

            var input by rememberSaveable { mutableStateOf("") }
            Row {
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Escribe...") }
                )
                Button(onClick = {
                    viewModel.sendMessage(chatId, input)
                    input = ""
                }) {
                    Text("Enviar")
                }
            }
        }
    }
}

@Composable
fun MessageBubble(
    message: Message,
    currentUserId: String,
    onDelete: (Message) -> Unit = {},
    viewModel: MessageViewModel
) {
    val isMine = message.senderId == currentUserId
    var expanded by rememberSaveable { mutableStateOf(false) }
    var isEditing by rememberSaveable { mutableStateOf(false) }
    var editText by rememberSaveable(message.id, isEditing) {
        mutableStateOf(
            message.content ?: ""
        )
    }
    val users by viewModel.users.collectAsState()


    val senderName = if (!isMine) {
        users.find { it.userId.toString() == message.senderId }?.userName
            ?: "Desconocido"
    } else null

    val sentAtFormatted = message.sentAtDate?.let {
        java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault()).format(it)
    } ?: message.sentAt


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            color = if (isMine) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(8.dp),
            tonalElevation = 2.dp,
            modifier = if (isMine) Modifier
                .combinedClickable(
                    onClick = {},
                    onLongClick = { expanded = true }
                ) else Modifier
        ) {
            if (isEditing) {
                Column(modifier = Modifier.padding(12.dp)) {
                    OutlinedTextField(
                        value = editText,
                        onValueChange = { editText = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row {
                        Button(onClick = {
                            isEditing = false
                            viewModel.editMessage(message.id.toString(), editText)
                        }) {
                            Text("Guardar")
                        }
                        Button(onClick = { isEditing = false }) {
                            Text("Cancelar")
                        }
                    }
                }
            } else {
                Card(
                    modifier = Modifier
                        .padding(8.dp),
                    shape = RoundedCornerShape(0.dp),
                    colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.Transparent)
                ) {
                    if (!isMine) {
                        Text(
                            senderName.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier
                        )
                    }
                    Text(
                        message.content.toString(),
                        modifier = Modifier,
                        color = if (isMine) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        sentAtFormatted.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier
                    )
                }
            }
            if (isMine) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Editar") },
                        onClick = {
                            expanded = false
                            isEditing = true
                            editText = message.content ?: ""
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Borrar") },
                        onClick = {
                            expanded = false
                            onDelete(message)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ChatInfoScreen(
    viewModel: MessageViewModel
) {
    val chat by viewModel.chat.collectAsState()
    val users by viewModel.users.collectAsState()
    val currentUserId by viewModel.currentUserId.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = chat?.name ?: "Chat",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            text = "Usuarios",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (viewModel.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn {
                items(users.size) { index ->
                    val user = users[index]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = "http://vms.iesluisvives.org:25003${user.profilePicture}",
                                contentDescription = "Foto de perfil",
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = user.userName,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f)
                            )

                            if (chat?.creatorId == currentUserId && user.userId != currentUserId) {
                                Button(
                                    onClick = {
                                        viewModel.removeUserFromChat(
                                            chat!!.id,
                                            user.userId
                                        )
                                    }
                                ) {
                                    Text("Eliminar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
