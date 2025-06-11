package com.example.gearhubmobile.data.models

/**
 * @author Rodrigo
 * @date 25 mayo, 2025
 */
data class LoginRequest(val email: String, val password: String)
data class RegisterRequest(val name: String, val password: String, val passwordRepeat: String, val email: String, val usertype: Int)
data class LoginResponse(val token: String)
data class ErrorResponse(val message: String)

