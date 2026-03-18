package com.sinaptix.smartsell.products

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinaptix.smartsell.data.domain.CategoryRepository
import com.sinaptix.smartsell.data.domain.ProductRepository
import com.sinaptix.smartsell.shared.domain.Category
import com.sinaptix.smartsell.shared.domain.Product
import com.sinaptix.smartsell.shared.util.RequestState
import kotlinx.coroutines.launch

class ProductsOverviewViewModel(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    var productsState: RequestState<List<Product>> by mutableStateOf(RequestState.Loading)
        private set
    var categoriesState: RequestState<List<Category>> by mutableStateOf(RequestState.Loading)
        private set
    var selectedCategoryId: String? by mutableStateOf(null)
        private set
    var searchQuery: String by mutableStateOf("")
        private set

    private val storeId = "default"

    init {
        loadCategories()
        loadProducts()
    }

    fun loadProducts(categoryId: String? = selectedCategoryId, search: String? = searchQuery.takeIf { it.isNotBlank() }) {
        viewModelScope.launch {
            productsState = RequestState.Loading
            val result = productRepository.getProducts(
                storeId = storeId,
                page = 1,
                categoryId = categoryId,
                search = search
            )
            productsState = when (result) {
                is RequestState.Success -> RequestState.Success(result.data.first)
                is RequestState.Error -> RequestState.Error(result.message)
                else -> RequestState.Error("Unknown error")
            }
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            categoriesState = RequestState.Loading
            categoriesState = categoryRepository.getCategories(storeId)
        }
    }

    fun onCategorySelected(categoryId: String?) {
        selectedCategoryId = categoryId
        loadProducts(categoryId = categoryId, search = searchQuery.takeIf { it.isNotBlank() })
    }

    fun onSearchQueryChanged(query: String) {
        searchQuery = query
        loadProducts(categoryId = selectedCategoryId, search = query.takeIf { it.isNotBlank() })
    }

    fun refresh() {
        loadCategories()
        loadProducts()
    }
}