package com.sinaptix.smartsell.shared.domain

import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val id: String,
    val orderNumber: String,
    val storeId: String,
    val items: List<OrderItem>,
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
data class OrderItem(
    val productId: String,
    val title: String,
    val thumbnail: String,
    val variantSku: String? = null,
    val variantSelection: Map<String, String> = emptyMap(),
    val specialInstructions: String? = null,
    val quantity: Int,
    val unitPrice: Double
)