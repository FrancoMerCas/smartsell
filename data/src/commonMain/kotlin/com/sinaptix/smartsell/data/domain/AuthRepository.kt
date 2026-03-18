package com.sinaptix.smartsell.data.domain

import com.sinaptix.smartsell.data.dto.AuthUser
import com.sinaptix.smartsell.shared.util.RequestState

interface AuthRepository {
    fun getCurrentUserId(): String?
    fun isLoggedIn(): Boolean
    suspend fun loginWithGoogle(idToken: String): RequestState<AuthUser>
    suspend fun loginWithEmail(email: String, password: String): RequestState<AuthUser>
    suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): RequestState<AuthUser>
    suspend fun signOut(): RequestState<Unit>
}