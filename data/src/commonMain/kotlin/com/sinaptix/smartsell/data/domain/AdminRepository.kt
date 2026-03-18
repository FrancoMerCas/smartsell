package com.sinaptix.smartsell.data.domain

import com.sinaptix.smartsell.data.dto.CreateCategoryRequest
import com.sinaptix.smartsell.data.dto.CreateProductRequest
import com.sinaptix.smartsell.data.dto.UpdateCategoryRequest
import com.sinaptix.smartsell.data.dto.UpdateProductRequest
import com.sinaptix.smartsell.shared.domain.AdminStats
import com.sinaptix.smartsell.shared.domain.Category
import com.sinaptix.smartsell.shared.domain.Order
import com.sinaptix.smartsell.shared.domain.Product
import com.sinaptix.smartsell.shared.util.RequestState

interface AdminRepository {
    suspend fun getStats(storeId: String): RequestState<AdminStats>
    suspend fun getAdminProducts(storeId: String, page: Int = 1): RequestState<Pair<List<Product>, Int>>
    suspend fun createProduct(request: CreateProductRequest): RequestState<Product>
    suspend fun updateProduct(productId: String, request: UpdateProductRequest): RequestState<Product>
    suspend fun deleteProduct(productId: String): RequestState<Unit>
    suspend fun getAdminOrders(storeId: String, status: String? = null, page: Int = 1): RequestState<Pair<List<Order>, Int>>
    suspend fun updateOrderStatus(orderId: String, status: String, note: String? = null): RequestState<Order>
    suspend fun createCategory(request: CreateCategoryRequest): RequestState<Category>
    suspend fun updateCategory(categoryId: String, request: UpdateCategoryRequest): RequestState<Category>
    suspend fun deleteCategory(categoryId: String): RequestState<Unit>
}
