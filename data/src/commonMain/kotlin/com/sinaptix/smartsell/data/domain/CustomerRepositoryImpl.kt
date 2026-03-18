package com.sinaptix.smartsell.data.domain

import com.sinaptix.smartsell.data.dto.PhoneNumberDto
import com.sinaptix.smartsell.data.dto.UpdateCustomerRequest
import com.sinaptix.smartsell.data.remote.ApiService
import com.sinaptix.smartsell.shared.domain.Customer
import com.sinaptix.smartsell.shared.domain.PhoneNumber
import com.sinaptix.smartsell.shared.util.RequestState

class CustomerRepositoryImpl(
    private val apiService: ApiService
) : CustomerRepository {

    override suspend fun getMyProfile(): RequestState<Customer> {
        return try {
            val result = apiService.getMyProfile()
            if (result.isSuccess) {
                val response = result.getOrNull()!!
                RequestState.Success(response.toDomain())
            } else {
                RequestState.Error(result.exceptionOrNull()?.message ?: "Failed to fetch profile")
            }
        } catch (e: Exception) {
            RequestState.Error("Error fetching profile: ${e.message}")
        }
    }

    override suspend fun updateProfile(customer: Customer): RequestState<Customer> {
        return try {
            val request = UpdateCustomerRequest(
                firstName = customer.firstName,
                lastName = customer.lastName,
                address = customer.address,
                city = customer.city,
                zip = customer.zip,
                phoneNumber = customer.phoneNumber?.let {
                    PhoneNumberDto(dialCode = it.dialCode, number = it.number)
                }
            )
            val result = apiService.updateMyProfile(request)
            if (result.isSuccess) {
                val response = result.getOrNull()!!
                RequestState.Success(response.toDomain())
            } else {
                RequestState.Error(result.exceptionOrNull()?.message ?: "Failed to update profile")
            }
        } catch (e: Exception) {
            RequestState.Error("Error updating profile: ${e.message}")
        }
    }
}

private fun com.sinaptix.smartsell.data.dto.CustomerResponse.toDomain(): Customer {
    return Customer(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        address = address,
        city = city,
        zip = zip,
        phoneNumber = phoneNumber?.let {
            PhoneNumber(dialCode = it.dialCode, number = it.number)
        }
    )
}