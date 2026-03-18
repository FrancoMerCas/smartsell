package com.sinaptix.smartsell.orders

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinaptix.smartsell.data.domain.OrderRepository
import com.sinaptix.smartsell.shared.domain.Order
import com.sinaptix.smartsell.shared.util.RequestState
import kotlinx.coroutines.launch

class OrderDetailViewModel(
    private val orderRepository: OrderRepository
) : ViewModel() {

    var orderState: RequestState<Order> by mutableStateOf(RequestState.Loading)
        private set

    fun loadOrder(orderId: String) {
        viewModelScope.launch {
            orderState = RequestState.Loading
            orderState = orderRepository.getOrderById(orderId)
        }
    }
}
