package com.sinaptix.smartsell.manage_product.module

import com.sinaptix.smartsell.manage_product.ManageProductViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val manageProductModule = module {
    viewModelOf(::ManageProductViewModel)
}
