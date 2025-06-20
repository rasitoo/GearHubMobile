package com.example.gearhubmobile.data.repositories


import android.content.Context
import com.example.gearhubmobile.data.apirest.AuthApi
import com.example.gearhubmobile.data.models.ErrorResponse
import com.example.gearhubmobile.data.models.LoginRequest
import com.example.gearhubmobile.data.models.LoginResponse
import com.example.gearhubmobile.data.models.RegisterRequest
import com.example.gearhubmobile.utils.SessionManager
import com.google.gson.Gson
import javax.inject.Inject

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */

class AuthRepository @Inject constructor(
    private val api: AuthApi,
    private val sessionManager: SessionManager
) {

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = api.login(LoginRequest(email, password))
            when (response.code()) {
                200 -> {
                    response.body()?.token?.let { token ->
                        sessionManager.saveToken(token)
                    }
                    Result.success(response.body()!!)
                }

                401 -> Result.failure(Exception("Credenciales incorrectas"))
                403 -> Result.failure(Exception("No tienes permiso"))
                500 -> Result.failure(Exception("Error de servidor"))
                else -> {
                    val errorBody = response.errorBody()?.string()
                    val message = try {
                        val error = Gson().fromJson(errorBody, ErrorResponse::class.java)
                        error.message
                    } catch (e: Exception) {
                        "Error inesperado: ${response.code()}"
                    }

                    Result.failure(Exception(message))
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de red: ${e.message}"))
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

    suspend fun logout(context: Context) {
        sessionManager.clearToken()
    }


}