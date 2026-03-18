package com.sinaptix.smartsell.orders

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinaptix.smartsell.data.domain.CartRepository
import com.sinaptix.smartsell.data.domain.CustomerRepository
import com.sinaptix.smartsell.data.domain.OrderRepository
import com.sinaptix.smartsell.data.dto.ShippingAddressDto
import com.sinaptix.smartsell.shared.domain.CartItem
import com.sinaptix.smartsell.shared.domain.Order
import com.sinaptix.smartsell.shared.util.RequestState
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val orderRepository: OrderRepository,
    private val cartRepository: CartRepository,
    private val customerRepository: CustomerRepository
) : ViewModel() {

    var fulfillmentType: String by mutableStateOf("delivery")
    var firstName: String by mutableStateOf("")
    var lastName: String by mutableStateOf("")
    var address: String by mutableStateOf("")
    var city: String by mutableStateOf("")
    var zip: String by mutableStateOf("")
    var phone: String by mutableStateOf("")
    var notes: String by mutableStateOf("")
    var placeOrderState: RequestState<Order> by mutableStateOf(RequestState.Idle)
        private set
    var cartItems: List<CartItem> by mutableStateOf(emptyList())
        private set

    val subtotal: Double
        get() = cartItems.sumOf { it.priceSnapshot * it.quantity }

    val shippingCost: Double
        get() = if (fulfillmentType != "delivery") 0.0 else if (subtotal > 500) 0.0 else 9.99

    val tax: Double
        get() = subtotal * 0.16

    val total: Double
        get() = subtotal + shippingCost + tax

    val isFormValid: Boolean
        get() = if (fulfillmentType == "delivery") {
            firstName.isNotBlank() && lastName.isNotBlank() && address.isNotBlank() &&
                    city.isNotBlank() && zip.isNotBlank() && phone.isNotBlank()
        } else true

    init {
        loadCart()
        prefillCustomerData()
    }

    private fun loadCart() {
        viewModelScope.launch {
            val result = cartRepository.getCart("default")
            if (result is RequestState.Success) {
                cartItems = result.data
            }
        }
    }

    private fun prefillCustomerData() {
        viewModelScope.launch {
            val result = customerRepository.getMyProfile()
            if (result is RequestState.Success) {
                val customer = result.data
                firstName = customer.firstName
                lastName = customer.lastName
                address = customer.address ?: ""
                city = customer.city ?: ""
                zip = customer.zip?.toString() ?: ""
                phone = customer.phoneNumber?.number ?: ""
            }
        }
    }

    fun placeOrder(storeId: String, onSuccess: (orderId: String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            placeOrderState = RequestState.Loading
            val shippingAddress = if (fulfillmentType == "delivery") {
                ShippingAddressDto(
                    firstName = firstName,
                    lastName = lastName,
                    address = address,
                    city = city,
                    zip = zip,
                    country = "MX",
                    phone = phone
                )
            } else null

            val result = orderRepository.placeOrder(
                storeId = storeId,
                fulfillmentType = fulfillmentType,
                shippingAddress = shippingAddress,
                notes = notes.takeIf { it.isNotBlank() }
            )
            when (result) {
                is RequestState.Success -> {
                    placeOrderState = result
                    onSuccess(result.data.id)
                }
                is RequestState.Error -> {
                    placeOrderState = result
                    onError(result.message)
                }
                else -> {}
            }
        }
    }
}
