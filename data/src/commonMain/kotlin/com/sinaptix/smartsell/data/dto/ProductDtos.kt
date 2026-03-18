package com.sinaptix.smartsell.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    val _id: String,
    val storeId: String,
    val title: String,
    val description: String,
    val thumbnail: String,
    val images: List<String> = emptyList(),
    val category: CategoryInProduct? = null,
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
    val isActive: Boolean = true,
    val isPopular: Boolean = false,
    val isNew: Boolean = false,
    val isDiscounted: Boolean = false,
    val rating: Double = 0.0,
    val reviewCount: Int = 0,
    val weight: Double? = null
)

@Serializable
data class CategoryInProduct(
    val _id: String,
    val name: String,
    val color: String = "#819A91"
)

@Serializable
data class VariantDimensionDto(
    val name: String,
    val values: List<String>
)

@Serializable
data class VariantCombinationDto(
    val sku: String,
    val dimensionValues: Map<String, String>,
    val price: Double,
    val originalPrice: Double? = null,
    val stock: Int,
    val isActive: Boolean = true
)

@Serializable
data class PaginatedProductsResponse(
    val products: List<ProductResponse>,
    val total: Int,
    val page: Int,
    val limit: Int,
    val pages: Int
)