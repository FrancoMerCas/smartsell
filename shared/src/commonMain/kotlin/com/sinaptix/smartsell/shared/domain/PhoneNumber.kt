package com.sinaptix.smartsell.shared.domain

import kotlinx.serialization.Serializable

@Serializable
data class PhoneNumber(
    val dialCode: Int = 0,
    val number: String
)
