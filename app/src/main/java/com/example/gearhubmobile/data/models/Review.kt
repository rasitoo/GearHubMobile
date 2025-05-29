package com.example.gearhubmobile.data.models


/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
data class Review(
    val id: String,
    val rating: Int,
    val comment: String,
    val userId: String,
    val response: ResponseDTO
    )

data class CreateReviewRequest(
    val workshopId: String,
    val rating: Int,
    val comment: String
)

data class UpdateReviewRequest(
    val id: String,
    val rating: Int,
    val comment: String
)
data class ReviewResponseDto(
    val id: String,
    val content: String,
    val creatorId: String,
    val threadId: String,
    val createdAt: String
)

data class CreateReviewResponseRequest(
    val content: String,
    val creatorId: String,
    val threadId: String
)

data class UpdateReviewResponseRequest(
    val id: String,
    val content: String
)
