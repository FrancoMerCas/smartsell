package com.sinaptix.smartsell.products.module

import com.sinaptix.smartsell.products.ProductDetailViewModel
import com.sinaptix.smartsell.products.ProductsOverviewViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val productsModule = module {
    viewModelOf(::ProductsOverviewViewModel)
    viewModelOf(::ProductDetailViewModel)
}