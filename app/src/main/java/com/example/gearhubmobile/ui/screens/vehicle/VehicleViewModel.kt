package com.example.gearhubmobile.ui.screens.vehicle

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gearhubmobile.data.models.User
import com.example.gearhubmobile.data.models.VehicleDetail
import com.example.gearhubmobile.data.repositories.ProfileRepository
import com.example.gearhubmobile.data.repositories.VehicleRepository
import com.example.gearhubmobile.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
@HiltViewModel
class VehicleViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val repository: VehicleRepository,
    private val sessionManager: SessionManager
) :
    ViewModel() {
    var user by mutableStateOf<User?>(null)
    var userId by mutableStateOf<String?>(null)
    val vehicles = mutableStateListOf<VehicleDetail>()
    var errorMessage by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)

    var currentIsWorkshop by mutableStateOf(false)
    var currentId by mutableStateOf<String?>(null)

    suspend fun getCurrentData() {
        currentIsWorkshop = sessionManager.getUserType() == 2
        currentId = sessionManager.getUserId().toString()
    }

    fun getUser(id: String?) {
        viewModelScope.launch {
            isLoading = true
            try {
                userId = id ?: sessionManager.getUserId()
                user = profileRepository.getUserById(userId.toString()).getOrNull()
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun loadVehicles(userId: String?) {
        viewModelScope.launch {
            isLoading = true
            try {
                val id = userId ?: sessionManager.getUserId().toString()
                val vehiclesTemp = repository.getVehicles()
                vehicles.removeAll(vehicles)

                vehiclesTemp.forEach { vehicle ->
                    if (vehicle.userId == id)
                        vehicles.add(repository.getVehicleById(vehicle.id))
                }
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun createVehicle(vin: String, brand: String, model: String, year: Int, license: String) {
        viewModelScope.launch {
            try {
                repository.createVehicle(vin, brand, model, year, license)
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }

}