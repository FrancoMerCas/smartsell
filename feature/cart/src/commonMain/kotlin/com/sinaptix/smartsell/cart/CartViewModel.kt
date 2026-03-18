package com.sinaptix.smartsell.cart

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinaptix.smartsell.data.domain.CartRepository
import com.sinaptix.smartsell.shared.domain.CartItem
import com.sinaptix.smartsell.shared.util.RequestState
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository: CartRepository
) : ViewModel() {

    var cartState: RequestState<List<CartItem>> by mutableStateOf(RequestState.Loading)
        private set

    private val storeId = "default"

    val subtotal: Double
        get() = (cartState as? RequestState.Success)?.data
            ?.sumOf { it.priceSnapshot * it.quantity } ?: 0.0

    val shippingCost: Double
        get() = if (subtotal > 500) 0.0 else 9.99

    val tax: Double
        get() = subtotal * 0.16

    val total: Double
        get() = subtotal + shippingCost + tax

    init {
        loadCart()
    }

    fun loadCart() {
        viewModelScope.launch {
            cartState = RequestState.Loading
            cartState = cartRepository.getCart(storeId)
        }
    }

    fun incrementQuantity(itemId: String, currentQty: Int) {
        viewModelScope.launch {
            val result = cartRepository.updateItemQuantity(storeId, itemId, currentQty + 1)
            if (result is RequestState.Success) {
                cartState = result
            }
        }
    }

    fun decrementQuantity(itemId: String, currentQty: Int) {
        if (currentQty <= 1) {
            removeItem(itemId)
            return
        }
        viewModelScope.launch {
            val result = cartRepository.updateItemQuantity(storeId, itemId, currentQty - 1)
            if (result is RequestState.Success) {
                cartState = result
            }
        }
    }

    fun removeItem(itemId: String) {
        viewModelScope.launch {
            val result = cartRepository.removeItem(storeId, itemId)
            if (result is RequestState.Success) {
                cartState = result
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            val result = cartRepository.clearCart(storeId)
            if (result is RequestState.Success) {
                cartState = RequestState.Success(emptyList())
            }
        }
    }
}