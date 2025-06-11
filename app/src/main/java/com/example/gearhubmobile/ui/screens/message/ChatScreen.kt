package com.example.gearhubmobile.ui.screens.message

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gearhubmobile.data.models.Message

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
@Composable
fun ChatDetailScreen(
    chatId: String,
    viewModel: MessageViewModel = hiltViewModel()
) {
    val messages by viewModel.messages.collectAsState()
    val currentUserId by viewModel.currentUserId.collectAsState()

    LaunchedEffect(chatId) {
        viewModel.connectToChat(chatId.toInt())
    }

    Column(modifier = Modifier.verticalScroll(rememberScrollState()).fillMaxSize().padding(8.dp)) {
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            reverseLayout = true
        ) {
            items(messages.reversed().size) { index ->
                val message = messages.reversed()[index]
                MessageBubble(message, currentUserId.toString(), onDelete = {viewModel.deleteMessage(message.id.toString())})
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
@Composable
fun MessageBubble(
    message: Message,
    currentUserId: String,
    onEdit: (Message, String) -> Unit = { _, _ -> },
    onDelete: (Message) -> Unit = {},
    viewModel: MessageViewModel = hiltViewModel()
) {
    val isMine = message.senderId == currentUserId
    var expanded by rememberSaveable { mutableStateOf(false) }
    var isEditing by rememberSaveable { mutableStateOf(false) }
    var editText by rememberSaveable { mutableStateOf(message.content ?: "") }

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
                            onEdit(message, editText)
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
                Text(
                    message.content.toString(),
                    modifier = Modifier.padding(12.dp),
                    color = if (isMine) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
            }
            if (isMine) {
                androidx.compose.material3.DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    androidx.compose.material3.DropdownMenuItem(
                        text = { Text("Editar") },
                        onClick = {
                            expanded = false
                            isEditing = true
                        }
                    )
                    androidx.compose.material3.DropdownMenuItem(
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

