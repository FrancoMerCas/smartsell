package com.sinaptix.smartsell.data.domain

import com.sinaptix.smartsell.data.dto.ProductResponse
import com.sinaptix.smartsell.data.remote.ApiService
import com.sinaptix.smartsell.shared.domain.Product
import com.sinaptix.smartsell.shared.domain.VariantCombination
import com.sinaptix.smartsell.shared.domain.VariantDimension
import com.sinaptix.smartsell.shared.util.RequestState

interface ProductRepository {
    suspend fun getProducts(
        storeId: String,
        page: Int = 1,
        categoryId: String? = null,
        search: String? = null
    ): RequestState<Pair<List<Product>, Int>>

    suspend fun getProductById(productId: String): RequestState<Product>
}

class ProductRepositoryImpl(
    private val apiService: ApiService
) : ProductRepository {

    override suspend fun getProducts(
        storeId: String,
        page: Int,
        categoryId: String?,
        search: String?
    ): RequestState<Pair<List<Product>, Int>> {
        return try {
            val result = apiService.getProducts(
                storeId = storeId,
                page = page,
                categoryId = categoryId,
                search = search
            )
            if (result.isSuccess) {
                val apiResponse = result.getOrNull()!!
                if (apiResponse.success && apiResponse.data != null) {
                    val products = apiResponse.data.products.map { it.toDomain() }
                    RequestState.Success(Pair(products, apiResponse.data.total))
                } else {
                    RequestState.Error(apiResponse.error?.message ?: "Failed to fetch products")
                }
            } else {
                RequestState.Error(result.exceptionOrNull()?.message ?: "Failed to fetch products")
            }
        } catch (e: Exception) {
            RequestState.Error("Error fetching products: ${e.message}")
        }
    }

    override suspend fun getProductById(productId: String): RequestState<Product> {
        return try {
            val result = apiService.getProductById(productId)
            if (result.isSuccess) {
                val apiResponse = result.getOrNull()!!
                if (apiResponse.success && apiResponse.data != null) {
                    RequestState.Success(apiResponse.data.toDomain())
                } else {
                    RequestState.Error(apiResponse.error?.message ?: "Failed to fetch product")
                }
            } else {
                RequestState.Error(result.exceptionOrNull()?.message ?: "Failed to fetch product")
            }
        } catch (e: Exception) {
            RequestState.Error("Error fetching product: ${e.message}")
        }
    }
}

fun ProductResponse.toDomain(): Product {
    return Product(
        id = _id,
        storeId = storeId,
        title = title,
        description = description,
        thumbnail = thumbnail,
        images = images,
        category = category?._id ?: "",
        categoryName = category?.name ?: "",
        productType = productType,
        hasVariants = hasVariants,
        variantDimensions = variantDimensions.map { VariantDimension(name = it.name, values = it.values) },
        variantCombinations = variantCombinations.map {
            VariantCombination(
                sku = it.sku,
                dimensionValues = it.dimensionValues,
                price = it.price,
                originalPrice = it.originalPrice,
                stock = it.stock,
                isActive = it.isActive
            )
        },
        price = price,
        originalPrice = originalPrice,
        discountPercent = discountPercent,
        stock = stock,
        preparationTimeMinutes = preparationTimeMinutes,
        allowsSpecialInstructions = allowsSpecialInstructions,
        isActive = isActive,
        isPopular = isPopular,
        isNew = isNew,
        isDiscounted = isDiscounted,
        rating = rating,
        reviewCount = reviewCount,
        weight = weight
    )
}