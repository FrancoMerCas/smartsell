package com.sinaptix.smartsell.shared.domain

import kotlinx.serialization.Serializable

@Serializable
data class Customer(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val address: String? = null,
    val city: String? = null,
    val zip: Int? = null,
    val phoneNumber: PhoneNumber? = null,
    val cart: List<CartItem> = emptyList()
)
