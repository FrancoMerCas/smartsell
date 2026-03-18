package com.sinaptix.smartsell.data.domain

import com.sinaptix.smartsell.data.remote.ApiService
import com.sinaptix.smartsell.shared.domain.Category
import com.sinaptix.smartsell.shared.util.RequestState

interface CategoryRepository {
    suspend fun getCategories(storeId: String): RequestState<List<Category>>
}

class CategoryRepositoryImpl(
    private val apiService: ApiService
) : CategoryRepository {

    override suspend fun getCategories(storeId: String): RequestState<List<Category>> {
        return try {
            val result = apiService.getCategories(storeId)
            if (result.isSuccess) {
                val apiResponse = result.getOrNull()!!
                if (apiResponse.success && apiResponse.data != null) {
                    val categories = apiResponse.data.map { dto ->
                        Category(
                            id = dto._id,
                            name = dto.name,
                            slug = dto.slug,
                            description = dto.description,
                            color = dto.color,
                            iconUrl = dto.iconUrl,
                            sortOrder = dto.sortOrder
                        )
                    }
                    RequestState.Success(categories)
                } else {
                    RequestState.Error(apiResponse.error?.message ?: "Failed to fetch categories")
                }
            } else {
                RequestState.Error(result.exceptionOrNull()?.message ?: "Failed to fetch categories")
            }
        } catch (e: Exception) {
            RequestState.Error("Error fetching categories: ${e.message}")
        }
    }
}