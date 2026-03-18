package com.sinaptix.smartsell.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AdminStatsResponse(
    val totalOrders: Int,
    val totalRevenue: Double,
    val pendingOrders: Int,
    val totalProducts: Int,
    val lowStockProducts: Int,
    val totalCustomers: Int
)

@Serializable
data class CreateProductRequest(
    val storeId: String,
    val title: String,
    val description: String,
    val thumbnail: String,
    val images: List<String> = emptyList(),
    val category: String,
    val productType: String = "physical",
    val hasVariants: Boolean = false,
    val variantDimensions: List<VariantDimensionDto> = emptyList(),
    val variantCombinations: List<VariantCombinationDto> = emptyList(),
    val price: Double = 0.0,
    val originalPrice: Double? = null,
    val discountPercent: Double? = null,
    val stock: Int = 0,
    val preparationTimeMinutes: Int? = null,
    val allowsSpecialInstructions: Boolean = false,
    val isPopular: Boolean = false,
    val isNew: Boolean = false,
    val isDiscounted: Boolean = false,
    val weight: Double? = null
)

@Serializable
data class UpdateProductRequest(
    val title: String? = null,
    val description: String? = null,
    val thumbnail: String? = null,
    val images: List<String>? = null,
    val category: String? = null,
    val price: Double? = null,
    val originalPrice: Double? = null,
    val discountPercent: Double? = null,
    val stock: Int? = null,
    val isActive: Boolean? = null,
    val isPopular: Boolean? = null,
    val isNew: Boolean? = null,
    val isDiscounted: Boolean? = null,
    val preparationTimeMinutes: Int? = null,
    val allowsSpecialInstructions: Boolean? = null,
    val weight: Double? = null
)

@Serializable
data class UpdateOrderStatusRequest(
    val status: String,
    val note: String? = null
)

@Serializable
data class CreateCategoryRequest(
    val storeId: String,
    val name: String,
    val slug: String,
    val description: String? = null,
    val color: String = "#819A91",
    val sortOrder: Int = 0
)

@Serializable
data class UpdateCategoryRequest(
    val name: String? = null,
    val description: String? = null,
    val color: String? = null,
    val sortOrder: Int? = null,
    val isActive: Boolean? = null
)
