package com.example.gearhubmobile.data.repositories


import com.example.gearhubmobile.data.apirest.AuthApi
import com.example.gearhubmobile.utils.SessionManager
import com.example.gearhubmobile.data.models.LoginRequest
import com.example.gearhubmobile.data.models.RegisterRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */

class AuthRepository @Inject constructor(
    private val api: AuthApi,
    private val sessionManager: SessionManager
) {

    suspend fun login(email: String, password: String): Boolean {
        return try {
            val response = api.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                response.body()?.token?.let { token ->
                    sessionManager.saveToken(token)
                    return true
                }
                false
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }


    suspend fun register(
        name: String,
        password: String,
        passwordRepeat: String,
        email: String,
        usertype: Int
    ): Boolean {
        return try {
            api.register(RegisterRequest(name, password, passwordRepeat, email, usertype))
            true
        } catch (e: Exception) {
            false
        }
    }
    suspend fun recover(
        email: String
    ): Boolean {
        return try {
            api.requestChange(email)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun logout() {
        sessionManager.clearToken()
    }


}