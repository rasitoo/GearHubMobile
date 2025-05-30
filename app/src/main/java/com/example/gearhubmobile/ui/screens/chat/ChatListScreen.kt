package com.example.gearhubmobile.ui.screens.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.gearhubmobile.data.models.Chat
import com.example.gearhubmobile.ui.screens.Screen

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
@Composable
fun ChatListScreen(
    navController: NavHostController,
    viewModel: ChatViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Chats", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        if (viewModel.isLoading) {
            CircularProgressIndicator()
        } else {
            LazyColumn {
                items(viewModel.chatList.size) { index ->
                    val chat = viewModel.chatList[index]
                    ChatItem(chat) {
                        navController.navigate(Screen.ChatDetail.createRoute(chat.id))
                    }
                    Divider()
                }
            }
        }
    }
}


@Composable
fun ChatItem(chat: Chat, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp)
    ) {
        Column {
            Text(chat.name.toString(), style = MaterialTheme.typography.titleMedium)
            //Text(chat.lastMessage, style = MaterialTheme.typography.bodySmall)
        }
    }
}
