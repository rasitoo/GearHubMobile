package com.example.gearhubmobile.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gearhubmobile.data.repositories.AuthRepository
import com.example.gearhubmobile.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Rodrigo
 * @date 25 mayo, 2025
 */
@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AuthRepository, internal val sessionManager: SessionManager
) : ViewModel() {

    val token = sessionManager.token

    var loginSuccess by mutableStateOf<Boolean?>(null)

    var isLoading by mutableStateOf(false)

    fun login(email: String, password: String) {
        viewModelScope.launch {
            isLoading = true
            loginSuccess = repository.login(email, password)
            isLoading = false
        }
    }

    fun register(email: String, password: String, name: String) {
        viewModelScope.launch {
            repository.register(email, password, name)
        }
    }
}