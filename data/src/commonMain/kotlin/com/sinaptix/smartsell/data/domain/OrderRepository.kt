package com.sinaptix.smartsell.data.domain

import com.sinaptix.smartsell.data.dto.OrderResponse
import com.sinaptix.smartsell.data.dto.PlaceOrderRequest
import com.sinaptix.smartsell.data.dto.ShippingAddressDto
import com.sinaptix.smartsell.data.remote.ApiService
import com.sinaptix.smartsell.shared.domain.Order
import com.sinaptix.smartsell.shared.domain.OrderItem
import com.sinaptix.smartsell.shared.util.RequestState

interface OrderRepository {
    suspend fun placeOrder(
        storeId: String,
        fulfillmentType: String,
        shippingAddress: ShippingAddressDto? = null,
        notes: String? = null
    ): RequestState<Order>

    suspend fun getMyOrders(page: Int = 1): RequestState<Pair<List<Order>, Int>>

    suspend fun getOrderById(orderId: String): RequestState<Order>
}

class OrderRepositoryImpl(
    private val apiService: ApiService
) : OrderRepository {

    override suspend fun placeOrder(
        storeId: String,
        fulfillmentType: String,
        shippingAddress: ShippingAddressDto?,
        notes: String?
    ): RequestState<Order> {
        return try {
            val request = PlaceOrderRequest(
                storeId = storeId,
                fulfillmentType = fulfillmentType,
                shippingAddress = shippingAddress,
                notes = notes
            )
            val result = apiService.placeOrder(request)
            if (result.isSuccess) {
                val apiResponse = result.getOrNull()!!
                if (apiResponse.success && apiResponse.data != null) {
                    RequestState.Success(apiResponse.data.toDomain())
                } else {
                    RequestState.Error(apiResponse.error?.message ?: "Failed to place order")
                }
            } else {
                RequestState.Error(result.exceptionOrNull()?.message ?: "Failed to place order")
            }
        } catch (e: Exception) {
            RequestState.Error("Error placing order: ${e.message}")
        }
    }

    override suspend fun getMyOrders(page: Int): RequestState<Pair<List<Order>, Int>> {
        return try {
            val result = apiService.getMyOrders(page = page)
            if (result.isSuccess) {
                val apiResponse = result.getOrNull()!!
                if (apiResponse.success && apiResponse.data != null) {
                    val orders = apiResponse.data.orders.map { it.toDomain() }
                    RequestState.Success(Pair(orders, apiResponse.data.total))
                } else {
                    RequestState.Error(apiResponse.error?.message ?: "Failed to fetch orders")
                }
            } else {
                RequestState.Error(result.exceptionOrNull()?.message ?: "Failed to fetch orders")
            }
        } catch (e: Exception) {
            RequestState.Error("Error fetching orders: ${e.message}")
        }
    }

    override suspend fun getOrderById(orderId: String): RequestState<Order> {
        return try {
            val result = apiService.getOrderById(orderId)
            if (result.isSuccess) {
                val apiResponse = result.getOrNull()!!
                if (apiResponse.success && apiResponse.data != null) {
                    RequestState.Success(apiResponse.data.toDomain())
                } else {
                    RequestState.Error(apiResponse.error?.message ?: "Failed to fetch order")
                }
            } else {
                RequestState.Error(result.exceptionOrNull()?.message ?: "Failed to fetch order")
            }
        } catch (e: Exception) {
            RequestState.Error("Error fetching order: ${e.message}")
        }
    }
}

private fun OrderResponse.toDomain(): Order {
    return Order(
        id = _id,
        orderNumber = orderNumber,
        storeId = storeId,
        items = items.map { item ->
            OrderItem(
                productId = item.productId,
                title = item.title,
                thumbnail = item.thumbnail,
                variantSku = item.variantSku,
                variantSelection = item.variantSelection,
                specialInstructions = item.specialInstructions,
                quantity = item.quantity,
                unitPrice = item.unitPrice
            )
        },
        fulfillmentType = fulfillmentType,
        subtotal = subtotal,
        tax = tax,
        shippingCost = shippingCost,
        discount = discount,
        total = total,
        status = status,
        paymentStatus = paymentStatus,
        notes = notes,
        createdAt = createdAt
    )
}