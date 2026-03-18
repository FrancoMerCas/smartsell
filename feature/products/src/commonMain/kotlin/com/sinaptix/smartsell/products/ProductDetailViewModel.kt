package com.sinaptix.smartsell.products

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinaptix.smartsell.data.domain.CartRepository
import com.sinaptix.smartsell.data.domain.ProductRepository
import com.sinaptix.smartsell.shared.domain.Product
import com.sinaptix.smartsell.shared.domain.VariantCombination
import com.sinaptix.smartsell.shared.util.RequestState
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    var productState: RequestState<Product> by mutableStateOf(RequestState.Loading)
        private set
    var selectedVariants: Map<String, String> by mutableStateOf(emptyMap())
        private set
    var quantity: Int by mutableStateOf(1)
        private set
    var specialInstructions: String by mutableStateOf("")
    var addToCartState: RequestState<Unit> by mutableStateOf(RequestState.Idle)
        private set

    private val product: Product?
        get() = (productState as? RequestState.Success)?.data

    val selectedVariantCombination: VariantCombination?
        get() = product?.variantCombinations?.find { combo ->
            combo.dimensionValues == selectedVariants && combo.isActive
        }

    val currentPrice: Double
        get() = selectedVariantCombination?.price ?: product?.price ?: 0.0

    val isInStock: Boolean
        get() = (selectedVariantCombination?.stock ?: product?.stock ?: 0) > 0

    fun loadProduct(productId: String) {
        viewModelScope.launch {
            productState = RequestState.Loading
            productState = productRepository.getProductById(productId)
        }
    }

    fun selectVariant(dimensionName: String, value: String) {
        selectedVariants = selectedVariants + (dimensionName to value)
    }

    fun incrementQuantity() {
        quantity = quantity + 1
    }

    fun decrementQuantity() {
        if (quantity > 1) quantity = quantity - 1
    }

    fun addToCart(storeId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val currentProduct = product ?: run {
            onError("Product not loaded")
            return
        }
        viewModelScope.launch {
            addToCartState = RequestState.Loading
            val result = cartRepository.addItem(
                storeId = storeId,
                productId = currentProduct.id,
                variantSku = selectedVariantCombination?.sku,
                variantSelection = selectedVariants,
                quantity = quantity,
                specialInstructions = specialInstructions.takeIf { it.isNotBlank() }
            )
            when (result) {
                is RequestState.Success -> {
                    addToCartState = RequestState.Success(Unit)
                    onSuccess()
                }
                is RequestState.Error -> {
                    addToCartState = RequestState.Error(result.message)
                    onError(result.message)
                }
                else -> {}
            }
        }
    }
}