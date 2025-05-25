package com.example.gearhubmobile.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gearhubmobile.data.repositories.AuthRepository
import com.example.gearhubmobile.utils.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * @author Rodrigo
 * @date 25 mayo, 2025
 */
class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

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

class AuthViewModelFactory(
    private val repository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
