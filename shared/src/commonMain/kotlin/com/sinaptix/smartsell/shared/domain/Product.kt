package com.sinaptix.smartsell.shared.domain

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: String,
    val storeId: String,
    val title: String,
    val description: String,
    val thumbnail: String,
    val images: List<String> = emptyList(),
    val category: String,
    val categoryName: String = "",
    val productType: String = "physical",
    val hasVariants: Boolean = false,
    val variantDimensions: List<VariantDimension> = emptyList(),
    val variantCombinations: List<VariantCombination> = emptyList(),
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
data class VariantDimension(
    val name: String,
    val values: List<String>
)

@Serializable
data class VariantCombination(
    val sku: String,
    val dimensionValues: Map<String, String>,
    val price: Double,
    val originalPrice: Double? = null,
    val stock: Int,
    val isActive: Boolean = true
)