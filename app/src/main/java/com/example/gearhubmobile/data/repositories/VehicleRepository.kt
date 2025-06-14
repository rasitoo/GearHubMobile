package com.example.gearhubmobile.data.repositories

import com.example.gearhubmobile.data.apirest.VehiclesApi
import com.example.gearhubmobile.data.models.Chat
import com.example.gearhubmobile.data.models.CreateChatRequest
import com.example.gearhubmobile.data.models.UpdateChatRequest
import com.example.gearhubmobile.data.models.Vehicle
import com.example.gearhubmobile.data.models.VehicleDetail
import com.example.gearhubmobile.data.models.VehiclePost
import com.example.gearhubmobile.data.models.VehicleUser
import com.example.gearhubmobile.data.models.VehicleUserDetail
import com.example.gearhubmobile.data.models.VehicleUserPost
import retrofit2.Response
import javax.inject.Inject

/**
 * @author Rodrigo
 * @date 14 junio, 2025
 */
class VehicleRepository @Inject constructor(private val api: VehiclesApi) {
    suspend fun getVehicles(): List<Vehicle> {
        return api.getVehicles()
    }

    suspend fun getVehicleById(id: String): VehicleDetail {
        return api.getVehicleById(id)
    }

    suspend fun createVehicle(
        vin: String,
        brand: String,
        model: String,
        year: Int,
        license: String
    ): Response<Vehicle> {
        val vehicleRequest = VehiclePost(vin, brand, model, year, license)
        return api.createVehicle(vehicleRequest)
    }

    suspend fun deleteVehicle(id: String): Response<Unit> {
        return api.deleteVehicle(id)
    }

    suspend fun updateVehicle(
        id: String,
        vin: String,
        brand: String,
        model: String,
        year: Int,
        license: String
    ) {
        val vehicleRequest = VehiclePost(vin, brand, model, year, license)
        return api.updateVehicle(id, vehicleRequest)
    }

    suspend fun getVehicleUsers(): List<VehicleUser> {
        return api.getVehicleUsers()
    }

    suspend fun getVehicleUserById(id: String): VehicleUserDetail {
        return api.getVehicleUserById(id)
    }

    suspend fun createVehicleUser(
        name: String,
        surname: String,
        dni: String
    ): Response<VehicleUser> {
        val vehicleRequest = VehicleUserPost(name, surname, dni)
        return api.createVehicleUser(vehicleRequest)
    }

    suspend fun deleteVehicleUser(id: String): Response<Unit> {
        return api.deleteVehicleUser(id)
    }

    suspend fun updateVehicleUser(
        id: String,
        name: String,
        surname: String,
        dni: String
    ) {
        val vehicleRequest = VehicleUserPost(name, surname, dni)
        return api.updateVehicleUser(id, vehicleRequest)
    }
}