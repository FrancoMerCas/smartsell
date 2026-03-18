package com.sinaptix.smartsell.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class GoogleAuthRequest(val idToken: String)

@Serializable
data class LoginRequest(val email: String, val password: String)

@Serializable
data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)

@Serializable
data class RefreshTokenRequest(val refreshToken: String)

@Serializable
data class AuthUser(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val role: String
)

@Serializable
data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val user: AuthUser
)

@Serializable
data class RefreshResponse(
    val accessToken: String,
    val refreshToken: String
)