package com.sinaptix.smartsell.admin_panel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinaptix.smartsell.data.domain.AdminRepository
import com.sinaptix.smartsell.shared.domain.AdminStats
import com.sinaptix.smartsell.shared.domain.Order
import com.sinaptix.smartsell.shared.domain.Product
import com.sinaptix.smartsell.shared.util.RequestState
import kotlinx.coroutines.launch

enum class AdminTab { Dashboard, Products, Orders }

class AdminPanelViewModel(
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val storeId = "default"

    var statsState: RequestState<AdminStats> by mutableStateOf(RequestState.Loading)
        private set

    var productsState: RequestState<List<Product>> by mutableStateOf(RequestState.Loading)
        private set

    var ordersState: RequestState<List<Order>> by mutableStateOf(RequestState.Loading)
        private set

    var selectedOrderFilter: String? by mutableStateOf(null)
        private set

    var selectedTab: AdminTab by mutableStateOf(AdminTab.Dashboard)
        private set

    var deleteProductState: RequestState<Unit> by mutableStateOf(RequestState.Idle)
        private set

    var updateOrderState: RequestState<Unit> by mutableStateOf(RequestState.Idle)
        private set

    init {
        loadStats()
        loadProducts()
        loadOrders()
    }

    fun loadStats() {
        viewModelScope.launch {
            statsState = RequestState.Loading
            statsState = adminRepository.getStats(storeId)
        }
    }

    fun loadProducts() {
        viewModelScope.launch {
            productsState = RequestState.Loading
            val result = adminRepository.getAdminProducts(storeId = storeId, page = 1)
            productsState = when (result) {
                is RequestState.Success -> RequestState.Success(result.data.first)
                is RequestState.Error -> RequestState.Error(result.message)
                else -> RequestState.Error("Unknown error")
            }
        }
    }

    fun loadOrders(status: String? = selectedOrderFilter) {
        viewModelScope.launch {
            ordersState = RequestState.Loading
            val result = adminRepository.getAdminOrders(storeId = storeId, status = status, page = 1)
            ordersState = when (result) {
                is RequestState.Success -> RequestState.Success(result.data.first)
                is RequestState.Error -> RequestState.Error(result.message)
                else -> RequestState.Error("Unknown error")
            }
        }
    }

    fun selectTab(tab: AdminTab) {
        selectedTab = tab
    }

    fun selectOrderFilter(status: String?) {
        selectedOrderFilter = status
        loadOrders(status = status)
    }

    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            deleteProductState = RequestState.Loading
            deleteProductState = when (val result = adminRepository.deleteProduct(productId)) {
                is RequestState.Success -> {
                    loadProducts()
                    RequestState.Success(Unit)
                }
                is RequestState.Error -> RequestState.Error(result.message)
                else -> RequestState.Error("Unknown error")
            }
        }
    }

    fun updateOrderStatus(orderId: String, status: String) {
        viewModelScope.launch {
            updateOrderState = RequestState.Loading
            val result = adminRepository.updateOrderStatus(orderId, status)
            updateOrderState = when (result) {
                is RequestState.Success -> {
                    loadOrders()
                    RequestState.Success(Unit)
                }
                is RequestState.Error -> RequestState.Error(result.message)
                else -> RequestState.Error("Unknown error")
            }
        }
    }

    fun refresh() {
        loadStats()
        loadProducts()
        loadOrders()
    }
}
