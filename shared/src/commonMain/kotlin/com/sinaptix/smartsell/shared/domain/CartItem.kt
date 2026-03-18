package com.sinaptix.smartsell.shared.domain

import kotlinx.serialization.Serializable

@Serializable
data class CartItem(
    val id: String,
    val productId: String,
    val productTitle: String = "",
    val productThumbnail: String = "",
    val variantSku: String? = null,
    val variantSelection: Map<String, String> = emptyMap(),
    val specialInstructions: String? = null,
    val quantity: Int,
    val priceSnapshot: Double
)