package com.example.gearhubmobile.data.apirest

import com.example.gearhubmobile.data.models.LoginRequest
import com.example.gearhubmobile.data.models.LoginResponse
import com.example.gearhubmobile.data.models.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
interface AuthApi {
    @POST("User/RegisterUsers")
    suspend fun register(@Body user: RegisterRequest): Response<Unit>

    @GET("User/RequestChangePassword/{email}")
    suspend fun requestChange(): Response<Unit>

    @POST("User/LogUser")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

}
