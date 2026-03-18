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

class OrderHistoryViewModel(
    private val orderRepository: OrderRepository
) : ViewModel() {

    var ordersState: RequestState<List<Order>> by mutableStateOf(RequestState.Loading)
        private set
    var currentPage: Int by mutableStateOf(1)
        private set
    var totalOrders: Int by mutableStateOf(0)
        private set

    init {
        loadOrders()
    }

    fun loadOrders(page: Int = 1) {
        viewModelScope.launch {
            ordersState = RequestState.Loading
            currentPage = page
            val result = orderRepository.getMyOrders(page = page)
            when (result) {
                is RequestState.Success -> {
                    ordersState = RequestState.Success(result.data.first)
                    totalOrders = result.data.second
                }
                is RequestState.Error -> {
                    ordersState = RequestState.Error(result.message)
                }
                else -> {}
            }
        }
    }

    fun refresh() {
        loadOrders(1)
    }
}
