package com.sinaptix.smartsell.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PhoneNumberDto(val dialCode: Int, val number: String)

@Serializable
data class UpdateCustomerRequest(
    val firstName: String,
    val lastName: String,
    val address: String? = null,
    val city: String? = null,
    val zip: Int? = null,
    val phoneNumber: PhoneNumberDto? = null
)

@Serializable
data class CustomerResponse(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val address: String? = null,
    val city: String? = null,
    val zip: Int? = null,
    val phoneNumber: PhoneNumberDto? = null,
    val role: String
)