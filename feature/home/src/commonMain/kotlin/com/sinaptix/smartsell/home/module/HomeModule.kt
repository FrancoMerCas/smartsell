package com.sinaptix.smartsell.home.module

import com.sinaptix.smartsell.home.HomeGraphViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homeModule = module {
    viewModelOf(::HomeGraphViewModel)
}