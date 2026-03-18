package com.sinaptix.smartsell.orders.module

import com.sinaptix.smartsell.orders.CheckoutViewModel
import com.sinaptix.smartsell.orders.OrderDetailViewModel
import com.sinaptix.smartsell.orders.OrderHistoryViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val ordersModule = module {
    viewModelOf(::CheckoutViewModel)
    viewModelOf(::OrderHistoryViewModel)
    viewModelOf(::OrderDetailViewModel)
}