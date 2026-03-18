package com.sinaptix.smartsell.data.domain

import com.sinaptix.smartsell.data.dto.AddCartItemRequest
import com.sinaptix.smartsell.data.dto.CartResponse
import com.sinaptix.smartsell.data.dto.UpdateCartItemRequest
import com.sinaptix.smartsell.data.remote.ApiService
import com.sinaptix.smartsell.shared.domain.CartItem
import com.sinaptix.smartsell.shared.util.RequestState

interface CartRepository {
    suspend fun getCart(storeId: String): RequestState<List<CartItem>>
    suspend fun addItem(
        storeId: String,
        productId: String,
        variantSku: String? = null,
        variantSelection: Map<String, String> = emptyMap(),
        quantity: Int,
        specialInstructions: String? = null
    ): RequestState<List<CartItem>>
    suspend fun updateItemQuantity(storeId: String, itemId: String, quantity: Int): RequestState<List<CartItem>>
    suspend fun removeItem(storeId: String, itemId: String): RequestState<List<CartItem>>
    suspend fun clearCart(storeId: String): RequestState<Unit>
}

class CartRepositoryImpl(
    private val apiService: ApiService
) : CartRepository {

    override suspend fun getCart(storeId: String): RequestState<List<CartItem>> {
        return try {
            val result = apiService.getCart(storeId)
            if (result.isSuccess) {
                val apiResponse = result.getOrNull()!!
                RequestState.Success(apiResponse.data?.toDomain() ?: emptyList())
            } else {
                RequestState.Error(result.exceptionOrNull()?.message ?: "Failed to fetch cart")
            }
        } catch (e: Exception) {
            RequestState.Error("Error fetching cart: ${e.message}")
        }
    }

    override suspend fun addItem(
        storeId: String,
        productId: String,
        variantSku: String?,
        variantSelection: Map<String, String>,
        quantity: Int,
        specialInstructions: String?
    ): RequestState<List<CartItem>> {
        return try {
            val request = AddCartItemRequest(
                productId = productId,
                variantSku = variantSku,
                variantSelection = variantSelection,
                quantity = quantity,
                specialInstructions = specialInstructions
            )
            val result = apiService.addCartItem(storeId, request)
            if (result.isSuccess) {
                val apiResponse = result.getOrNull()!!
                RequestState.Success(apiResponse.data?.toDomain() ?: emptyList())
            } else {
                RequestState.Error(result.exceptionOrNull()?.message ?: "Failed to add item")
            }
        } catch (e: Exception) {
            RequestState.Error("Error adding item: ${e.message}")
        }
    }

    override suspend fun updateItemQuantity(storeId: String, itemId: String, quantity: Int): RequestState<List<CartItem>> {
        return try {
            val result = apiService.updateCartItem(storeId, itemId, UpdateCartItemRequest(quantity = quantity))
            if (result.isSuccess) {
                val apiResponse = result.getOrNull()!!
                RequestState.Success(apiResponse.data?.toDomain() ?: emptyList())
            } else {
                RequestState.Error(result.exceptionOrNull()?.message ?: "Failed to update item")
            }
        } catch (e: Exception) {
            RequestState.Error("Error updating item: ${e.message}")
        }
    }

    override suspend fun removeItem(storeId: String, itemId: String): RequestState<List<CartItem>> {
        return try {
            val result = apiService.removeCartItem(storeId, itemId)
            if (result.isSuccess) {
                val apiResponse = result.getOrNull()!!
                RequestState.Success(apiResponse.data?.toDomain() ?: emptyList())
            } else {
                RequestState.Error(result.exceptionOrNull()?.message ?: "Failed to remove item")
            }
        } catch (e: Exception) {
            RequestState.Error("Error removing item: ${e.message}")
        }
    }

    override suspend fun clearCart(storeId: String): RequestState<Unit> {
        return try {
            val result = apiService.clearCart(storeId)
            if (result.isSuccess) {
                RequestState.Success(Unit)
            } else {
                RequestState.Error(result.exceptionOrNull()?.message ?: "Failed to clear cart")
            }
        } catch (e: Exception) {
            RequestState.Error("Error clearing cart: ${e.message}")
        }
    }
}

private fun CartResponse.toDomain(): List<CartItem> {
    return items.map { item ->
        CartItem(
            id = item._id,
            productId = item.productId._id,
            productTitle = item.productId.title,
            productThumbnail = item.productId.thumbnail,
            variantSku = item.variantSku,
            variantSelection = item.variantSelection,
            specialInstructions = item.specialInstructions,
            quantity = item.quantity,
            priceSnapshot = item.priceSnapshot
        )
    }
}