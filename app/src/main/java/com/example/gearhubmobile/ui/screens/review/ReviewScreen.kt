package com.example.gearhubmobile.ui.screens.review

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gearhubmobile.ui.navigation.Routes
import kotlinx.coroutines.launch

/**
 * @author Rodrigo
 * @date 15 junio, 2025
 */

@Composable
fun ReviewsScreen(
    userId: String?,
    viewModel: ReviewViewModel,
    navController: NavHostController
) {
    LaunchedEffect(Unit) {
        viewModel.getCurrentData()
        viewModel.loadReviews(userId ?: viewModel.currentId)
        viewModel.getUser(userId ?: viewModel.currentId)
    }
    val reviews = viewModel.reviews
    val user = viewModel.user
    val coroutineScope = rememberCoroutineScope()


    val totalReviews = reviews.size
    val averageRating = if (totalReviews > 0) reviews.map { it.rating }.average() else 0.0

    Scaffold(
        floatingActionButton = {
            if (!viewModel.currentIsWorkshop)
                FloatingActionButton(onClick = {
                    navController.navigate(Routes.ADD_REVIEW)
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir reseña")
                }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(padding)
        ) {
            Text(
                "Reseñas de ${user?.userName ?: ""}",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                StarRating(rating = averageRating)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    String.format("%.1f", averageRating),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    "($totalReviews reseñas)",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (reviews.isEmpty()) {
                Text("No hay reseñas aún.")
            } else {
                LazyColumn {
                    items(reviews) { review ->
                        var expanded by rememberSaveable(review.id) { mutableStateOf(false) }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .combinedClickable(
                                    onClick = {},
                                    onLongClick = {
                                        if (!viewModel.currentIsWorkshop) {
                                            expanded = true
                                        }
                                    }
                                ),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    StarRating(rating = review.rating.toDouble())
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "${review.rating}/5",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    review.comment,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                if (review.response != null) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Surface(
                                        color = MaterialTheme.colorScheme.secondaryContainer,
                                        shape = MaterialTheme.shapes.small,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            "Respuesta: ${review.response.message}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                                            modifier = Modifier.padding(8.dp)
                                        )
                                    }
                                } else {
                                    if (
                                        viewModel.currentIsWorkshop && viewModel.currentId == user?.userId
                                    ) {
                                        var responseText by rememberSaveable(review.id) {
                                            mutableStateOf(
                                                ""
                                            )
                                        }
                                        OutlinedTextField(
                                            value = responseText,
                                            onValueChange = { responseText = it },
                                            label = { Text("Responder a la reseña") },
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Button(
                                            onClick = {
                                                coroutineScope.launch {
                                                    viewModel.responder(review.id, responseText)
                                                    responseText = ""
                                                    viewModel.loadReviews(viewModel.currentId)
                                                }
                                            },
                                            enabled = responseText.isNotBlank()
                                        ) {
                                            Text("Responder")
                                        }
                                    }
                                }
                                val userRev = viewModel.userReviews[review.userId] ?: review.userId
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "Por $userRev",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                                )

                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Borrar reseña") },
                                    onClick = {
                                        expanded = false
                                        viewModel.deleteReview(review.id)
                                    }
                                )
                            }
                        }
                    }

                }
            }
        }
    }
}


@Composable
fun StarRating(rating: Double, maxRating: Int = 5) {
    Row {
        for (i in 1..maxRating) {
            Icon(
                imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = null,
                tint = if (i <= rating) Color(0xFFFFC107) else Color.LightGray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReviewScreen(
    viewModel: ReviewViewModel,
    navController: NavHostController
) {
    var rating by rememberSaveable { mutableIntStateOf(0) }
    var comment by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf<String?>(viewModel.errorMessage) }
    var isSubmitting by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Añadir reseña") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .padding(padding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Tu valoración", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                for (i in 1..5) {
                    IconButton(onClick = { rating = i }) {
                        Icon(
                            imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                            contentDescription = null,
                            tint = if (i <= rating) Color(0xFFFFC107) else Color.LightGray,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text("Comentario") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5
            )
            Spacer(modifier = Modifier.height(16.dp))
            viewModel.errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
            }
            Button(
                onClick = {
                    if (rating == 0) {
                        error = "Selecciona una puntuación"
                    } else if (comment.isBlank()) {
                        error = "Escribe un comentario"
                    } else {
                        isSubmitting = true
                        error = null
                    }
                },
                enabled = !isSubmitting,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar reseña")
            }

        }
    }
    LaunchedEffect(isSubmitting) {
        if (isSubmitting)
            viewModel.addReview(viewModel.user?.userId ?: "-1", rating, comment)
    }
}