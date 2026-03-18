package com.sinaptix.smartsell.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinaptix.smartsell.data.domain.AuthRepository
import com.sinaptix.smartsell.shared.util.RequestState
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    var screenState: RequestState<Unit> by mutableStateOf(RequestState.Idle)
        private set

    fun loginWithGoogle(
        idToken: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            screenState = RequestState.Loading
            val result = authRepository.loginWithGoogle(idToken)
            if (result.isSuccess()) {
                screenState = RequestState.Success(Unit)
                onSuccess()
            } else {
                val message = result.getErrorMessage()
                screenState = RequestState.Error(message)
                onError(message)
            }
        }
    }

    fun loginWithEmail(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            screenState = RequestState.Loading
            val result = authRepository.loginWithEmail(email, password)
            if (result.isSuccess()) {
                screenState = RequestState.Success(Unit)
                onSuccess()
            } else {
                val message = result.getErrorMessage()
                screenState = RequestState.Error(message)
                onError(message)
            }
        }
    }

    fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            screenState = RequestState.Loading
            val result = authRepository.register(firstName, lastName, email, password)
            if (result.isSuccess()) {
                screenState = RequestState.Success(Unit)
                onSuccess()
            } else {
                val message = result.getErrorMessage()
                screenState = RequestState.Error(message)
                onError(message)
            }
        }
    }
}