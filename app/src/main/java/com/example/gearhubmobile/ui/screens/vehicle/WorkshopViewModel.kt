package com.example.gearhubmobile.ui.screens.vehicle

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gearhubmobile.data.models.ResponseDTO
import com.example.gearhubmobile.data.models.Thread
import com.example.gearhubmobile.data.models.User
import com.example.gearhubmobile.data.repositories.ProfileRepository
import com.example.gearhubmobile.data.repositories.ResponseRepository
import com.example.gearhubmobile.data.repositories.ThreadRepository
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
class WorkshopViewModel @Inject constructor(
    private val repository: VehicleRepository
) :
    ViewModel() {

}