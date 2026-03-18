package com.sinaptix.smartsell.shared.domain

data class AdminStats(
    val totalOrders: Int,
    val totalRevenue: Double,
    val pendingOrders: Int,
    val totalProducts: Int,
    val lowStockProducts: Int,
    val totalCustomers: Int
)
