package com.sinaptix.smartsell.auth.module

import com.sinaptix.smartsell.auth.AuthViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authModule = module {
    viewModelOf(::AuthViewModel)
}