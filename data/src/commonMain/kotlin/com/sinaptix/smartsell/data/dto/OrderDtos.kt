package com.sinaptix.smartsell.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PlaceOrderRequest(
    val storeId: String,
    val fulfillmentType: String,
    val shippingAddress: ShippingAddressDto? = null,
    val notes: String? = null
)

@Serializable
data class ShippingAddressDto(
    val firstName: String,
    val lastName: String,
    val address: String,
    val city: String,
    val state: String? = null,
    val zip: String,
    val country: String,
    val phone: String
)

@Serializable
data class OrderResponse(
    val _id: String,
    val orderNumber: String,
    val storeId: String,
    val items: List<OrderItemResponse>,
    val fulfillmentType: String,
    val subtotal: Double,
    val tax: Double,
    val shippingCost: Double,
    val discount: Double = 0.0,
    val total: Double,
    val status: String,
    val paymentStatus: String,
    val notes: String? = null,
    val createdAt: String = ""
)

@Serializable
data class OrderItemResponse(
    val productId: String,
    val title: String,
    val thumbnail: String,
    val variantSku: String? = null,
    val variantSelection: Map<String, String> = emptyMap(),
    val specialInstructions: String? = null,
    val quantity: Int,
    val unitPrice: Double
)

@Serializable
data class PaginatedOrdersResponse(
    val orders: List<OrderResponse>,
    val total: Int,
    val page: Int,
    val limit: Int,
    val pages: Int
)