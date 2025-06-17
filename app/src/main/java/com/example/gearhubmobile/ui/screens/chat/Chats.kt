package com.example.gearhubmobile.ui.screens.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gearhubmobile.data.models.Chat

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
@Composable
fun ChatListScreen(
    navController: NavHostController,
    onChatClick: (String) -> Unit,
    onChatEdit: (String) -> Unit,
    viewModel: ChatViewModel
) {
    val chatList by viewModel.chatList.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.connectToChatList()
        viewModel.getCurrentUserId()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Chats",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = {
                navController.navigate("usersList")
            }) {
                Icon(Icons.Default.Add, contentDescription = "Crear chat")
            }
        }
        Spacer(Modifier.height(16.dp))
        LaunchedEffect(Unit) {
            viewModel.loadChats()
        }

        if (viewModel.isLoading) {
            CircularProgressIndicator()
        } else {
            LazyColumn {
                items(chatList.size) { index ->
                    val chat = chatList[index]
                    ChatItem(chat = chat, onClick = { onChatClick(chat.id.toString()) },viewModel, onEdit = {chat -> onChatEdit(chat.id)})
                }
            }
        }
    }
}

@Composable
fun ChatItem(
    chat: Chat,
    onClick: (String) -> Unit,
    viewModel: ChatViewModel,
    onEdit: (Chat) -> Unit
) {
    var expanded by rememberSaveable(chat.id) { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .combinedClickable(
                onClick = { onClick(chat.id.toString()) },
                onLongClick = {
                    if (chat.creatorId == viewModel.currentUserId) {
                        expanded = true
                    }
                }
            ),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = chat.name.toString(),
                style = MaterialTheme.typography.titleMedium
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Editar chat") },
                onClick = {
                    expanded = false
                    onEdit(chat)
                }
            )
            DropdownMenuItem(
                text = { Text("Eliminar chat") },
                onClick = {
                    expanded = false
                    viewModel.deleteChat(chat.id)
                }
            )
        }
    }
}

@Composable
fun SelectUsersScreen(
    viewModel: ChatViewModel,
    navController: NavHostController
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadUsers()
    }
    val users = viewModel.users
    val selectedUsers = viewModel.usersSelected

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar usuarios") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.isLoading) {
            CircularProgressIndicator()
        } else
            LazyColumn {
                users.value.filter { it.userName.contains(searchQuery, ignoreCase = true) }
                    .forEach { user ->
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = selectedUsers.contains(user),
                                    onCheckedChange = {
                                        if (it) selectedUsers.add(user)
                                        else selectedUsers.remove(user)
                                    }
                                )

                                Spacer(modifier = Modifier.width(8.dp))
                                Text(user.userName)
                            }
                            Divider()
                        }
                    }
            }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("createChat")
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedUsers.isNotEmpty() == true
        ) {
            Text("Confirmar selección")
        }
    }
}

@Composable
fun CreateChatScreen(
    viewModel: ChatViewModel,
    navController: NavHostController
) {
    var chatName by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Crear nuevo chat", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = chatName,
            onValueChange = { chatName = it },
            label = { Text("Nombre del chat") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (chatName.isBlank()) {
                    error = "El nombre no puede estar vacío"
                } else {
                    viewModel.createChat(chatName)
                    navController.navigate("chats")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear")
        }

        if (error != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = error!!, color = Color.Red)
        }
    }
}

@Composable
fun EditChatScreen(
    viewModel: ChatViewModel,
    navController: NavHostController,
    id: String
) {
    val chatState = viewModel.chat.collectAsState()
    var chatName by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(chatState.value?.name) {
        chatState.value?.name?.let { chatName = it }
    }
    LaunchedEffect(Unit) {
        viewModel.loadChat(id)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Editar chat", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = chatName,
            onValueChange = { chatName = it },
            label = { Text("Nombre del chat") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (chatName.isBlank()) {
                    error = "El nombre no puede estar vacío"
                } else {
                    viewModel.editChat(chatName, id)
                    navController.navigate("chats")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Editar")
        }

        if (error != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = error!!, color = Color.Red)
        }
    }
}
