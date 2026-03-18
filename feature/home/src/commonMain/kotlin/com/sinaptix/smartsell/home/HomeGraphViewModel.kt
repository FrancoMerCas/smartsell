package com.sinaptix.smartsell.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinaptix.smartsell.data.domain.AuthRepository
import kotlinx.coroutines.launch

class HomeGraphViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun signOut(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val result = authRepository.signOut()
            if (result.isSuccess()) {
                onSuccess()
            } else if (result.isError()) {
                onError(result.getErrorMessage())
            }
        }
    }
}