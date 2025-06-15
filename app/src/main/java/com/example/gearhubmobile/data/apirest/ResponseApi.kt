package com.example.gearhubmobile.data.apirest

/**
 * @author Rodrigo
 * @date 27 mayo, 2025
 */
import com.example.gearhubmobile.data.models.CreateResponseRequest
import com.example.gearhubmobile.data.models.ResponseDTO
import com.example.gearhubmobile.data.models.ResponseRequest
import com.example.gearhubmobile.data.models.UpdateResponseRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ResponseApi {

    @POST("api/Responses/CrearRespuesta")
    suspend fun createResponse(@Body request: CreateResponseRequest): retrofit2.Response<Unit>

    @GET("api/Responses/HasLike/{responseId}")
    suspend fun hasLike(@Path("responseId") responseId: String): Boolean

    @POST("api/Responses/LikeResponse/{responseId}")
    suspend fun likeResponse(@Path("responseId") responseId: String): retrofit2.Response<Unit>

    @DELETE("api/Responses/UnlikeResponse/{responseId}")
    suspend fun unlikeResponse(@Path("responseId") responseId: String): retrofit2.Response<Unit>

    @PUT("api/Responses/UpdateaRespuesta")
    suspend fun updateResponse(@Body request: UpdateResponseRequest): retrofit2.Response<Unit>

    @GET("api/Responses/byLike/{creatorId}")
    suspend fun getResponsesByLikes(@Path("creatorId") creatorId: String): ResponseRequest

    @GET("api/Responses/bycreator/{creatorId}")
    suspend fun getResponsesByCreator(@Path("creatorId") creatorId: String): ResponseRequest

    @GET("api/Responses/{idResponse}")
    suspend fun getResponseById(@Path("idResponse") id: String): ResponseDTO

    @GET("api/Responses/{threadId}/Responses")
    suspend fun getResponsesByThread(@Path("threadId") threadId: String): ResponseRequest

    @GET("api/Responses/{threadId}/Responses")
    suspend fun getResponsesByResponse(
        @Path("threadId") threadId: String,
        @Query("parentId") parentId: String
    ): ResponseRequest
}
