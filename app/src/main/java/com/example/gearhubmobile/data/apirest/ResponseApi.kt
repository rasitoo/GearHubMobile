package com.example.gearhubmobile.data.apirest

/**
 * @author Rodrigo
 * @date 27 mayo, 2025
 */
import com.example.gearhubmobile.data.models.CreateResponseRequest
import com.example.gearhubmobile.data.models.Response
import com.example.gearhubmobile.data.models.UpdateResponseRequest
import retrofit2.http.*

interface ResponseApi {

    @POST("api/Responses/CrearRespuesta")
    suspend fun createResponse(@Body request: CreateResponseRequest)

    @GET("api/Responses/HasLike/{responseId}")
    suspend fun hasLike(@Path("responseId") responseId: String): Boolean

    @POST("api/Responses/LikeResponse/{responseId}")
    suspend fun likeResponse(@Path("responseId") responseId: String)

    @DELETE("api/Responses/UnlikeResponse/{responseId}")
    suspend fun unlikeResponse(@Path("responseId") responseId: String)

    @PUT("api/Responses/UpdateaRespuesta")
    suspend fun updateResponse(@Body request: UpdateResponseRequest)

    @GET("api/Responses/byLike/{creatorId}")
    suspend fun getResponsesByLikes(@Path("creatorId") creatorId: String): List<Response>

    @GET("api/Responses/bycreator/{creatorId}")
    suspend fun getResponsesByCreator(@Path("creatorId") creatorId: String): List<Response>

    @GET("api/Responses/{idResponse}")
    suspend fun getResponseById(@Path("idResponse") id: String): Response

    @GET("api/Responses/{threadId}/Responses")
    suspend fun getResponsesByThread(@Path("threadId") threadId: String): List<Response>
}
