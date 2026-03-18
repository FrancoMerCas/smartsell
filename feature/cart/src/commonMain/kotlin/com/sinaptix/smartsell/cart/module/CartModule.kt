package com.sinaptix.smartsell.cart.module

import com.sinaptix.smartsell.cart.CartViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val cartModule = module {
    viewModelOf(::CartViewModel)
}