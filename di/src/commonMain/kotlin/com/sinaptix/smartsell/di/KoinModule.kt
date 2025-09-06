package com.sinaptix.smartsell.di

import com.sinaptix.smartsell.auth.AuthViewModel
import com.sinaptix.smartsell.data.domain.CustomerRepository
import com.sinaptix.smartsell.data.domain.CustomerRepositoryImpl
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val shareModule = module {
    single<CustomerRepository> { CustomerRepositoryImpl() }
    viewModelOf(::AuthViewModel)
}
fun initializeKoin(
    config: (KoinApplication.() ->Unit)? = null
) {
    startKoin{
        config?.invoke(this)
        modules(shareModule)
    }
}