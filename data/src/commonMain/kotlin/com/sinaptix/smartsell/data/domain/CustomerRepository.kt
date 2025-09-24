package com.sinaptix.smartsell.data.domain

import com.sinaptix.smartsell.shared.domain.Customer
import com.sinaptix.smartsell.shared.util.RequestState
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {
    fun getCurrentUserId(): String?

    suspend fun createCustomer(
        user: FirebaseUser?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    fun readCustomerFlow(): Flow<RequestState<Customer>>
    suspend fun setCustomer(
        customer: Customer,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )
    suspend fun updateCustomer(
        customer: Customer,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )
    suspend fun signOut(): RequestState<Unit>
}