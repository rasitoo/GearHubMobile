package com.example.gearhubmobile.data.apirest

import com.example.gearhubmobile.data.models.Vehicle
import com.example.gearhubmobile.data.models.VehicleDetail
import com.example.gearhubmobile.data.models.VehiclePost
import com.example.gearhubmobile.data.models.VehicleUser
import com.example.gearhubmobile.data.models.VehicleUserDetail
import com.example.gearhubmobile.data.models.VehicleUserPost
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * @author Rodrigo
 * @date 14 junio, 2025
 */
interface VehiclesApi {
    @GET("api/users")
    suspend fun getVehicleUsers(): List<VehicleUser>

    @GET("api/users/{JWTid}")
    suspend fun getVehicleUserById(@Path("id") id: String): VehicleUserDetail

    @POST("api/users")
    suspend fun createVehicleUser(@Body request: VehicleUserPost): Response<VehicleUser>

    @PUT("api/users/{id}")
    suspend fun updateVehicleUser(@Path("id") id: String, @Body request: VehicleUserPost)

    @DELETE("api/users/{id}")
    suspend fun deleteVehicleUser(@Path("id") id: String): Response<Unit>

    @GET("api/vehicles")
    suspend fun getVehicles(): List<Vehicle>

    @GET("api/vehicles/{id}")
    suspend fun getVehicleById(@Path("id") id: String): VehicleDetail

    @POST("api/vehicles")
    suspend fun createVehicle(@Body request: VehiclePost): Response<Vehicle>

    @PUT("api/vehicles/{id}")
    suspend fun updateVehicle(@Path("id") id: String, @Body request: VehiclePost)

    @DELETE("api/vehicles/{id}")
    suspend fun deleteVehicle(@Path("id") id: String): Response<Unit>
}