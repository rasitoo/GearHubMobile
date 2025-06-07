package com.example.gearhubmobile.ui.screens.login

import android.util.Base64
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gearhubmobile.data.repositories.AuthRepository
import com.example.gearhubmobile.data.repositories.ProfileRepository
import com.example.gearhubmobile.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

/**
 * @author Rodrigo
 * @date 25 mayo, 2025
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val userRepository: ProfileRepository,
    internal val sessionManager: SessionManager
) : ViewModel() {

    val userid = sessionManager.token.map { extractUserIdFromToken(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")
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

    fun register(
        name: String,
        password: String,
        passwordRepeat: String,
        email: String,
        isWorkshop: Boolean
    ) {
        viewModelScope.launch {
            repository.register(name, password, passwordRepeat, email, if (isWorkshop) 2 else 1)
        }
    }

    fun recover(email: String) {
        viewModelScope.launch {
            repository.recover(email)
        }
    }

    fun isTokenExpired(token: String?): Boolean {
        if (token.isNullOrBlank()) return true
        val parts = token.split(".")
        if (parts.size < 2) return true
        val payload =
            String(Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP))
        val json = JSONObject(payload)
        val exp = json.optLong("exp", 0L)
        val now = System.currentTimeMillis() / 1000
        return exp < now
    }

    fun extractUserIdFromToken(token: String?): String {
        if (token.isNullOrBlank()) return ""
        Log.d("erase", token)
        val parts = token.split(".")
        if (parts.size < 2) return ""
        val payload =
            String(Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP))
        val json = JSONObject(payload)
        return json.optString("nameid", "")
    }

    suspend fun checkUserStatus(): String {
        val result = userRepository.getUserById(userid.value)
        if (result.isSuccess) {
            return "OK"
        } else if (result.exceptionOrNull()?.message == "NOT_FOUND") {
            return "NOT_FOUND"
        } else if (result.exceptionOrNull()?.message == "UNAUTHORIZED") {
            return "UNAUTHORIZED"
        } else {
            return "UNKNOWN"
        }
    }
}