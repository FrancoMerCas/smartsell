package com.sinaptix.smartsell.data.domain

import com.sinaptix.smartsell.shared.domain.Customer
import com.sinaptix.smartsell.shared.util.RequestState

interface CustomerRepository {
    suspend fun getMyProfile(): RequestState<Customer>
    suspend fun updateProfile(customer: Customer): RequestState<Customer>
}