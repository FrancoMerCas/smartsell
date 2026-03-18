package com.sinaptix.smartsell.data.domain

import com.sinaptix.smartsell.data.dto.AuthUser
import com.sinaptix.smartsell.data.remote.ApiService
import com.sinaptix.smartsell.shared.util.RequestState
import com.sinaptix.smartsell.shared.util.TokenStorage

class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val tokenStorage: TokenStorage
) : AuthRepository {

    override fun getCurrentUserId(): String? {
        val token = tokenStorage.getAccessToken() ?: return null
        return decodeJwtSub(token)
    }

    override fun isLoggedIn(): Boolean {
        return tokenStorage.isLoggedIn()
    }

    override suspend fun loginWithGoogle(idToken: String): RequestState<AuthUser> {
        return try {
            val result = apiService.googleAuth(idToken)
            if (result.isSuccess) {
                val authResponse = result.getOrNull()!!
                tokenStorage.saveTokens(authResponse.accessToken, authResponse.refreshToken)
                RequestState.Success(authResponse.user)
            } else {
                RequestState.Error(result.exceptionOrNull()?.message ?: "Google sign-in failed")
            }
        } catch (e: Exception) {
            RequestState.Error("Google sign-in error: ${e.message}")
        }
    }

    override suspend fun loginWithEmail(email: String, password: String): RequestState<AuthUser> {
        return try {
            val result = apiService.login(email, password)
            if (result.isSuccess) {
                val authResponse = result.getOrNull()!!
                tokenStorage.saveTokens(authResponse.accessToken, authResponse.refreshToken)
                RequestState.Success(authResponse.user)
            } else {
                RequestState.Error(result.exceptionOrNull()?.message ?: "Login failed")
            }
        } catch (e: Exception) {
            RequestState.Error("Login error: ${e.message}")
        }
    }

    override suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): RequestState<AuthUser> {
        return try {
            val result = apiService.register(firstName, lastName, email, password)
            if (result.isSuccess) {
                val authResponse = result.getOrNull()!!
                tokenStorage.saveTokens(authResponse.accessToken, authResponse.refreshToken)
                RequestState.Success(authResponse.user)
            } else {
                RequestState.Error(result.exceptionOrNull()?.message ?: "Registration failed")
            }
        } catch (e: Exception) {
            RequestState.Error("Registration error: ${e.message}")
        }
    }

    override suspend fun signOut(): RequestState<Unit> {
        return try {
            apiService.logout()
            tokenStorage.clearTokens()
            RequestState.Success(Unit)
        } catch (e: Exception) {
            tokenStorage.clearTokens()
            RequestState.Success(Unit)
        }
    }

    /**
     * Decodes the `sub` claim from a JWT payload without a third-party library.
     * JWT format: header.payload.signature (Base64Url encoded parts)
     */
    private fun decodeJwtSub(token: String): String? {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return null
            val payload = base64UrlDecode(parts[1])
            // Simple extraction of "sub":"<value>"
            val subRegex = Regex(""""sub"\s*:\s*"([^"]+)"""")
            subRegex.find(payload)?.groupValues?.getOrNull(1)
        } catch (e: Exception) {
            null
        }
    }

    private fun base64UrlDecode(input: String): String {
        val padded = when (input.length % 4) {
            2 -> "$input=="
            3 -> "$input="
            else -> input
        }
        val standard = padded.replace('-', '+').replace('_', '/')
        val decoded = decodeBase64(standard)
        return decoded.decodeToString()
    }

    private fun decodeBase64(input: String): ByteArray {
        val table = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
        val clean = input.trimEnd('=')
        val outputLength = clean.length * 6 / 8
        val output = ByteArray(outputLength)
        var buffer = 0
        var bitsLeft = 0
        var index = 0
        for (char in clean) {
            val value = table.indexOf(char)
            if (value < 0) continue
            buffer = (buffer shl 6) or value
            bitsLeft += 6
            if (bitsLeft >= 8) {
                bitsLeft -= 8
                if (index < outputLength) {
                    output[index++] = ((buffer shr bitsLeft) and 0xFF).toByte()
                }
            }
        }
        return output
    }
}