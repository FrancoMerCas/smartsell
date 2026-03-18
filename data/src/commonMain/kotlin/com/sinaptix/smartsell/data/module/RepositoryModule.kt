package com.sinaptix.smartsell.data.module

import com.russhwolf.settings.Settings
import com.sinaptix.smartsell.data.domain.AuthRepository
import com.sinaptix.smartsell.data.domain.AuthRepositoryImpl
import com.sinaptix.smartsell.data.domain.CustomerRepository
import com.sinaptix.smartsell.data.domain.CustomerRepositoryImpl
import com.sinaptix.smartsell.data.remote.ApiService
import com.sinaptix.smartsell.shared.util.TokenStorage
import com.sinaptix.smartsell.shared.util.TokenStorageImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<Settings> { Settings() }
    single<TokenStorage> { TokenStorageImpl(get()) }
    single { ApiService(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<CustomerRepository> { CustomerRepositoryImpl(get()) }
}