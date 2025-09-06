package com.sinaptix.smartsell.shared.domain

import kotlinx.serialization.Serializable

@Serializable
data class PhoneNumber(
    val dialCode: Int,
    val number: String
)
