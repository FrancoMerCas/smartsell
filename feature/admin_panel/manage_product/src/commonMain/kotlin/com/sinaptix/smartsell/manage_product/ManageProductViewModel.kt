package com.sinaptix.smartsell.manage_product

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinaptix.smartsell.data.domain.AdminRepository
import com.sinaptix.smartsell.data.domain.CategoryRepository
import com.sinaptix.smartsell.data.domain.ProductRepository
import com.sinaptix.smartsell.data.dto.CreateProductRequest
import com.sinaptix.smartsell.data.dto.UpdateProductRequest
import com.sinaptix.smartsell.shared.domain.Category
import com.sinaptix.smartsell.shared.util.RequestState
import kotlinx.coroutines.launch

class ManageProductViewModel(
    private val adminRepository: AdminRepository,
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val storeId = "default"

    // Form state
    var title: String by mutableStateOf("")
    var description: String by mutableStateOf("")
    var thumbnailUrl: String by mutableStateOf("")
    var selectedCategoryId: String by mutableStateOf("")
    var selectedCategoryName: String by mutableStateOf("")
    var productType: String by mutableStateOf("physical")
    var price: String by mutableStateOf("")
    var originalPrice: String by mutableStateOf("")
    var stock: String by mutableStateOf("")
    var weight: String by mutableStateOf("")
    var preparationTime: String by mutableStateOf("")
    var allowsSpecialInstructions: Boolean by mutableStateOf(false)
    var isPopular: Boolean by mutableStateOf(false)
    var isNew: Boolean by mutableStateOf(false)
    var isDiscounted: Boolean by mutableStateOf(false)

    // Categories
    var categoriesState: RequestState<List<Category>> by mutableStateOf(RequestState.Loading)
    var showCategoryPicker: Boolean by mutableStateOf(false)

    // Submit state
    var submitState: RequestState<Unit> by mutableStateOf(RequestState.Idle)
    var loadProductState: RequestState<Unit> by mutableStateOf(RequestState.Idle)

    var isEditMode: Boolean = false
        private set
    private var editingProductId: String? = null

    val isFormValid: Boolean
        get() = title.isNotBlank() &&
                description.isNotBlank() &&
                thumbnailUrl.isNotBlank() &&
                selectedCategoryId.isNotBlank() &&
                price.toDoubleOrNull() != null &&
                (price.toDoubleOrNull() ?: 0.0) > 0 &&
                (productType == "digital" || stock.toIntOrNull() != null)

    val productTypes = listOf("physical", "digital", "food", "service")

    init {
        loadCategories()
    }

    fun initForEdit(productId: String) {
        isEditMode = true
        editingProductId = productId
        loadProductForEdit(productId)
    }

    private fun loadProductForEdit(productId: String) {
        viewModelScope.launch {
            loadProductState = RequestState.Loading
            val result = productRepository.getProductById(productId)
            when (result) {
                is RequestState.Success -> {
                    val product = result.data
                    title = product.title
                    description = product.description
                    thumbnailUrl = product.thumbnail
                    selectedCategoryId = product.category
                    selectedCategoryName = product.categoryName
                    productType = product.productType
                    price = product.price.toString()
                    originalPrice = product.originalPrice?.toString() ?: ""
                    stock = product.stock.toString()
                    weight = product.weight?.toString() ?: ""
                    preparationTime = product.preparationTimeMinutes?.toString() ?: ""
                    allowsSpecialInstructions = product.allowsSpecialInstructions
                    isPopular = product.isPopular
                    isNew = product.isNew
                    isDiscounted = product.isDiscounted
                    loadProductState = RequestState.Success(Unit)
                }
                is RequestState.Error -> {
                    loadProductState = RequestState.Error(result.message)
                }
                else -> {}
            }
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            categoriesState = RequestState.Loading
            categoriesState = categoryRepository.getCategories(storeId)
        }
    }

    fun selectCategory(category: Category) {
        selectedCategoryId = category.id
        selectedCategoryName = category.name
        showCategoryPicker = false
    }

    fun toggleCategoryPicker() {
        showCategoryPicker = !showCategoryPicker
    }

    fun saveProduct(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            submitState = RequestState.Loading
            val priceDouble = price.toDoubleOrNull() ?: run {
                submitState = RequestState.Idle
                onError("Invalid price")
                return@launch
            }

            if (isEditMode) {
                val editId = editingProductId ?: run {
                    submitState = RequestState.Idle
                    onError("No product ID for edit")
                    return@launch
                }
                val request = UpdateProductRequest(
                    title = title.takeIf { it.isNotBlank() },
                    description = description.takeIf { it.isNotBlank() },
                    thumbnail = thumbnailUrl.takeIf { it.isNotBlank() },
                    category = selectedCategoryId.takeIf { it.isNotBlank() },
                    price = priceDouble,
                    originalPrice = originalPrice.toDoubleOrNull(),
                    stock = stock.toIntOrNull(),
                    isPopular = isPopular,
                    isNew = isNew,
                    isDiscounted = isDiscounted,
                    preparationTimeMinutes = preparationTime.toIntOrNull(),
                    allowsSpecialInstructions = allowsSpecialInstructions,
                    weight = weight.toDoubleOrNull()
                )
                when (val result = adminRepository.updateProduct(editId, request)) {
                    is RequestState.Success -> {
                        submitState = RequestState.Success(Unit)
                        onSuccess()
                    }
                    is RequestState.Error -> {
                        submitState = RequestState.Idle
                        onError(result.message)
                    }
                    else -> {}
                }
            } else {
                val request = CreateProductRequest(
                    storeId = storeId,
                    title = title,
                    description = description,
                    thumbnail = thumbnailUrl,
                    category = selectedCategoryId,
                    productType = productType,
                    price = priceDouble,
                    originalPrice = originalPrice.toDoubleOrNull(),
                    stock = stock.toIntOrNull() ?: 0,
                    isPopular = isPopular,
                    isNew = isNew,
                    isDiscounted = isDiscounted,
                    preparationTimeMinutes = preparationTime.toIntOrNull(),
                    allowsSpecialInstructions = allowsSpecialInstructions,
                    weight = weight.toDoubleOrNull()
                )
                when (val result = adminRepository.createProduct(request)) {
                    is RequestState.Success -> {
                        submitState = RequestState.Success(Unit)
                        onSuccess()
                    }
                    is RequestState.Error -> {
                        submitState = RequestState.Idle
                        onError(result.message)
                    }
                    else -> {}
                }
            }
        }
    }
}
