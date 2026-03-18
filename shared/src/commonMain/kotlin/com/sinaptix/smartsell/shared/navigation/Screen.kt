package com.sinaptix.smartsell.shared.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Auth : Screen()
    @Serializable
    data object HomeGraph : Screen()
    @Serializable
    data object ProductsOverview : Screen()
    @Serializable
    data object Cart : Screen()
    @Serializable
    data object Categories : Screen()
    @Serializable
    data object Profile : Screen()
    @Serializable
    data object AdminPanel : Screen()
    @Serializable
    data class ManageProduct(val id: String? = null) : Screen()
    @Serializable
    data class ProductDetail(val productId: String) : Screen()
    @Serializable
    data object Checkout : Screen()
    @Serializable
    data object OrderHistory : Screen()
    @Serializable
    data class OrderDetail(val orderId: String) : Screen()
}