package com.example.gearhubmobile.data.repositories

import com.example.gearhubmobile.data.apirest.ReviewApi
import com.example.gearhubmobile.data.models.CreateReviewRequest
import com.example.gearhubmobile.data.models.CreateReviewResponseRequest
import com.example.gearhubmobile.data.models.Review
import com.example.gearhubmobile.data.models.ReviewResponseDto
import com.example.gearhubmobile.data.models.UpdateReviewRequest
import com.example.gearhubmobile.data.models.UpdateReviewResponseRequest
import retrofit2.Response
import javax.inject.Inject

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
class ReviewRepository @Inject constructor(private val api: ReviewApi) {

    suspend fun getResponseById(id: String): ReviewResponseDto {
        return api.getResponseById(id)
    }

    suspend fun getReviewById(id: String): Review {
        return api.getReviewById(id)
    }

    suspend fun getAverageReview(id: String): Double {
        return api.getAverageReview(id)
    }

    suspend fun getReviewByUserAndWorkshop(id: String, workshopid: String): Review {
        return api.getReviewByUserAndWorkshop(id, workshopid)
    }

    suspend fun getReviewsByWorkshop(id: String): List<Review> {
        return api.getReviewsByWorkshop(id)
    }

    suspend fun deleteReview(id: String): Response<Unit> {
        return api.deleteReview(id)
    }

    suspend fun deleteResponse(id: String): Response<Unit> {
        return api.deleteResponse(id)
    }

    suspend fun countReviews(id: String): Int {
        return api.countReviews(id)
    }

    suspend fun createResponse(
        content: String,
        creatorId: String,
        threadId: String,
    ): Response<Unit> {
        val reviewRequest = CreateReviewResponseRequest(content, creatorId, threadId)

        return api.createResponse(reviewRequest)
    }

    suspend fun createReview(workshopId: String, rating: Int, comment: String): Response<Unit> {
        val reviewRequest = CreateReviewRequest(workshopId, rating, comment)

        return api.createReview(reviewRequest)
    }

    suspend fun updateResponse(id: String, content: String): Response<Unit> {
        val reviewRequest = UpdateReviewResponseRequest(id, content)

        return api.updateResponse(reviewRequest)
    }

    suspend fun updateReview(id: String, rating: Int, comment: String): Response<Unit> {
        val reviewRequest = UpdateReviewRequest(id, rating, comment)

        return api.updateReview(reviewRequest)
    }

}