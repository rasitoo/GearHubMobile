package com.example.gearhubmobile.data.apirest

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
import com.example.gearhubmobile.data.models.CreateReviewRequest
import com.example.gearhubmobile.data.models.CreateReviewResponseRequest
import com.example.gearhubmobile.data.models.Review
import com.example.gearhubmobile.data.models.ReviewResponseDto
import com.example.gearhubmobile.data.models.UpdateReviewRequest
import com.example.gearhubmobile.data.models.UpdateReviewResponseRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReviewApi {

    @POST("api/Reviews")
    suspend fun createReview(@Body request: CreateReviewRequest): Response<Unit>

    @GET("api/Reviews/CountReviews/{workshopId}")
    suspend fun countReviews(@Path("workshopId") workshopId: String): Int

    @GET("api/Reviews/GetMediaReview/{workshopId}")
    suspend fun getAverageReview(@Path("workshopId") workshopId: String): Double

    @GET("api/Reviews/GetReview/{userId}/{workshopId}")
    suspend fun getReviewByUserAndWorkshop(
        @Path("userId") userId: String,
        @Path("workshopId") workshopId: String
    ): Review

    @GET("api/Reviews/GetReviewById/{id}")
    suspend fun getReviewById(@Path("id") id: String): Review

    @GET("api/Reviews/GetReviewsByWorkshop/{workshopId}")
    suspend fun getReviewsByWorkshop(@Path("workshopId") workshopId: String): List<Review>

    @PUT("api/Reviews/ReviewUpdate")
    suspend fun updateReview(@Body request: UpdateReviewRequest): Response<Unit>

    @DELETE("api/Reviews/{id}")
    suspend fun deleteReview(@Path("id") id: String): Response<Unit>

    @POST("api/Response/CreateResponse")
    suspend fun createResponse(@Body request: CreateReviewResponseRequest): Response<Unit>

    @DELETE("api/Response/DeleteResponse/{responseId}")
    suspend fun deleteResponse(@Path("responseId") responseId: String): Response<Unit>

    @GET("api/Response/GetResponseById/{id}")
    suspend fun getResponseById(@Path("id") id: String): ReviewResponseDto

    @PUT("api/Response/UpdateResponse")
    suspend fun updateResponse(@Body request: UpdateReviewResponseRequest): Response<Unit>
}
