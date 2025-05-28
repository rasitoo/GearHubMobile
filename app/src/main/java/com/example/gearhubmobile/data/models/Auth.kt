package com.example.gearhubmobile.data.models

/**
 * @author Rodrigo
 * @date 25 mayo, 2025
 */
data class LoginRequest(val email: String, val password: String)
data class RegisterRequest(val email: String, val password: String, val name: String)
data class LoginResponse(val token: String)
