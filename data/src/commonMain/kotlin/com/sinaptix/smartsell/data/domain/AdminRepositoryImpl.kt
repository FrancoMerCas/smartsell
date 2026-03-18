package com.sinaptix.smartsell.data.domain

import com.sinaptix.smartsell.data.dto.CategoryResponse
import com.sinaptix.smartsell.data.dto.CreateCategoryRequest
import com.sinaptix.smartsell.data.dto.CreateProductRequest
import com.sinaptix.smartsell.data.dto.OrderResponse
import com.sinaptix.smartsell.data.dto.UpdateCategoryRequest
import com.sinaptix.smartsell.data.dto.UpdateProductRequest
import com.sinaptix.smartsell.data.remote.ApiService
import com.sinaptix.smartsell.shared.domain.AdminStats
import com.sinaptix.smartsell.shared.domain.Category
import com.sinaptix.smartsell.shared.domain.Order
import com.sinaptix.smartsell.shared.domain.OrderItem
import com.sinaptix.smartsell.shared.domain.Product
import com.sinaptix.smartsell.shared.util.RequestState

class AdminRepositoryImpl(
    private val apiService: ApiService
) : AdminRepository {

    override suspend fun getStats(storeId: String): RequestState<AdminStats> {
        return try {
            val result = apiService.getAdminStats(storeId)
            if (result.isSuccess) {
                val apiResponse = result.getOrNull()!!
                if (apiResponse.success && apiResponse.data != null) {
                    val dto = apiResponse.data
                    RequestState.Success(
                        AdminStats(
                            totalOrders = dto.totalOrders,
                            totalRevenue = dto.totalRevenue,
                            pendingOrders = dto.pendingOrders,
                            totalProducts = dto.totalProducts,
                            lowStockProducts = dto.lowStockProducts,
                            totalCustomers = dto.totalCustomers
                        )
                    )
                } else {
                    RequestState.Error(apiResponse.error?.message ?: "Failed to fetch stats")
                }
            } else {
                RequestState.Error(result.exceptionOrNull()?.message ?: "Failed to fetch stats")
            }
        } catch (e: Exception) {
            RequestState.Error("Error fetching stats: ${e.message}")
        }
    }

    override suspend fun getAdminProducts(storeId: String, page: Int): RequestState<Pair<List<Product>, Int>> {
        return try {
            val result = apiService.getAdminProducts(storeId = storeId, page = page)
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

    override suspend fun createProduct(request: CreateProductRequest): RequestState<Product> {
        return try {
            val result = apiService.createProduct(request)
            if (result.isSuccess) {
                val apiResponse = result.getOrNull()!!
                if (apiResponse.success && apiResponse.data != null) {
                    RequestState.Success(apiResponse.data.toDomain())
                } else {
                    RequestState.Error(apiResponse.error?.message ?: "Failed to create product")
                }
            } else {
                RequestState.Error(result.exceptionOrNull()?.message ?: "Failed to create product")
            }
        } catch (e: Exception) {
            RequestState.Error("Error creating product: ${e.message}")
        }
    }

    override suspend fun updateProduct(productId: String, request: UpdateProductRequest): RequestState<Product> {
        return try {
            val result = apiService.updateProduct(productId, request)
            if (result.isSuccess) {
                val apiResponse = result.getOrNull()!!
                if (apiResponse.success && apiResponse.data != null) {
                    RequestState.Success(apiResponse.data.toDomain())
                } else {
                    RequestState.Error(apiResponse.error?.message ?: "Failed to update product")
                }
            } else {
                RequestState.Error(result.exceptionOrNull()?.message ?: "Failed to update product")
            }
        } catch (e: Exception) {
            RequestState.Error("Error updating product: ${e.message}")
        }
    }

    override suspend fun deleteProduct(productId: String): RequestState<Unit> {
        return try {
            val result = apiService.deleteProduct(productId)
            if (result.isSuccess) {
                val apiResponse = result.getOrNull()!!
                if (apiResponse.success) {
                    RequestState.Success(Unit)
                } else {
                    RequestState.Error(apiResponse.error?.message ?: "Failed to delete product")
                }
            } else {
                RequestState.Error(result.exceptionOrNull()?.message ?: "Failed to delete product")
            }
        } catch (e: Exception) {
            RequestState.Error("Error deleting product: ${e.message}")
        }
    }

    override suspend fun getAdminOrders(storeId: String, status: String?, page: Int): RequestState<Pair<List<Order>, Int>> {
        return try {
            val result = apiService.getAdminOrders(storeId = storeId, status = status, page = page)
            if (result.isSuccess) {
                val apiResponse = result.getOrNull()!!
                if (apiResponse.success && apiResponse.data != null) {
                    val orders = apiResponse.data.orders.map { it.toAdminDomain() }
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

    override suspend fun updateOrderStatus(orderId: String, status: String, note: String?): RequestState<Order> {
        return try {
            val result = apiService.updateOrderStatus(orderId, status, note)
            if (result.isSuccess) {
                val apiResponse = result.getOrNull()!!
                if (apiResponse.success && apiResponse.data != null) {
                    RequestState.Success(apiResponse.data.toAdminDomain())
                } else {
                    RequestState.Error(apiResponse.error?.message ?: "Failed to update order status")
                }
            } else {
                RequestState.Error(result.exceptionOrNull()?.message ?: "Failed to update order status")
            }
        } catch (e: Exception) {
            RequestState.Error("Error updating order status: ${e.message}")
        }
    }

    override suspend fun createCategory(request: CreateCategoryRequest): RequestState<Category> {
        return try {
            val result = apiService.createCategory(request)
            if (result.isSuccess) {
                val apiResponse = result.getOrNull()!!
                if (apiResponse.success && apiResponse.data != null) {
                    RequestState.Success(apiResponse.data.toCategoryDomain())
                } else {
                    RequestState.Error(apiResponse.error?.message ?: "Failed to create category")
                }
            } else {
                RequestState.Error(result.exceptionOrNull()?.message ?: "Failed to create category")
            }
        } catch (e: Exception) {
            RequestState.Error("Error creating category: ${e.message}")
        }
    }

    override suspend fun updateCategory(categoryId: String, request: UpdateCategoryRequest): RequestState<Category> {
        return try {
            val result = apiService.updateCategory(categoryId, request)
            if (result.isSuccess) {
                val apiResponse = result.getOrNull()!!
                if (apiResponse.success && apiResponse.data != null) {
                    RequestState.Success(apiResponse.data.toCategoryDomain())
                } else {
                    RequestState.Error(apiResponse.error?.message ?: "Failed to update category")
                }
            } else {
                RequestState.Error(result.exceptionOrNull()?.message ?: "Failed to update category")
            }
        } catch (e: Exception) {
            RequestState.Error("Error updating category: ${e.message}")
        }
    }

    override suspend fun deleteCategory(categoryId: String): RequestState<Unit> {
        return try {
            val result = apiService.deleteCategory(categoryId)
            if (result.isSuccess) {
                val apiResponse = result.getOrNull()!!
                if (apiResponse.success) {
                    RequestState.Success(Unit)
                } else {
                    RequestState.Error(apiResponse.error?.message ?: "Failed to delete category")
                }
            } else {
                RequestState.Error(result.exceptionOrNull()?.message ?: "Failed to delete category")
            }
        } catch (e: Exception) {
            RequestState.Error("Error deleting category: ${e.message}")
        }
    }
}

private fun OrderResponse.toAdminDomain(): Order {
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

private fun CategoryResponse.toCategoryDomain(): Category {
    return Category(
        id = _id,
        name = name,
        slug = slug,
        description = description,
        color = color,
        iconUrl = iconUrl,
        sortOrder = sortOrder
    )
}
