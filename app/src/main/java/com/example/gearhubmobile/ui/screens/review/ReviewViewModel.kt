package com.example.gearhubmobile.ui.screens.review

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.gearhubmobile.data.models.Review
import com.example.gearhubmobile.data.models.User
import com.example.gearhubmobile.data.repositories.ProfileRepository
import com.example.gearhubmobile.data.repositories.ReviewRepository
import com.example.gearhubmobile.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author Rodrigo
 * @date 15 junio, 2025
 */
@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val reviewRepository: ReviewRepository,
    private val sessionManager: SessionManager
) :
    ViewModel() {

    var user by mutableStateOf<User?>(null)
    val reviews = mutableStateListOf<Review>()
    var errorMessage by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)
    var userReviews by mutableStateOf<Map<String, String?>>(emptyMap())


    suspend fun loadReviews(id: String?) {
        if (id == null) return
        isLoading = true
        errorMessage = null
        reviews.clear()

        try {
            val result = reviewRepository.getReviewsByWorkshop(id)
            reviews.addAll(result)

            userReviews = reviews.mapNotNull { review ->
                profileRepository.getUserById(review.userId).getOrNull()?.let { user ->
                    review.userId to user.userName
                }
            }.toMap()
            isLoading = false

        } catch (e: Exception) {

            errorMessage = "Error al cargar reseñas"
            isLoading = false

        }

    }

    suspend fun addReview(id: String, rating: Int, comment: String) {
        isLoading = true
        errorMessage = null

        try {
            val result = reviewRepository.createReview(id, rating, comment)
            if (!result.isSuccessful)
                errorMessage = result.message()
        } catch (e: Exception) {
            errorMessage = "Error al cargar reseñas"
            isLoading = false
        }
    }

    suspend fun getUser(userId: String?) {
        if (userId == null) return
        isLoading = true
        errorMessage = null

        try {
            val result = profileRepository.getUserById(userId)

            user = result.getOrNull()
            isLoading = false

        } catch (e: Exception) {
            errorMessage = "Error al cargar usuario"
            isLoading = false

        }

    }
}