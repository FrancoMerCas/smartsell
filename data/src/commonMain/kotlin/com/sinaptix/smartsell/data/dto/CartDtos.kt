package com.sinaptix.smartsell.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CartItemResponse(
    val _id: String,
    val productId: ProductInCart,
    val variantSku: String? = null,
    val variantSelection: Map<String, String> = emptyMap(),
    val specialInstructions: String? = null,
    val quantity: Int,
    val priceSnapshot: Double
)

@Serializable
data class ProductInCart(
    val _id: String,
    val title: String,
    val thumbnail: String,
    val price: Double
)

@Serializable
data class CartResponse(
    val _id: String,
    val userId: String,
    val storeId: String,
    val items: List<CartItemResponse>
)

@Serializable
data class AddCartItemRequest(
    val productId: String,
    val variantSku: String? = null,
    val variantSelection: Map<String, String> = emptyMap(),
    val quantity: Int,
    val specialInstructions: String? = null
)

@Serializable
data class UpdateCartItemRequest(
    val quantity: Int
)