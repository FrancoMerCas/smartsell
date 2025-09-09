package com.sinaptix.smartsell.data.module

import com.sinaptix.smartsell.data.domain.CustomerRepository
import com.sinaptix.smartsell.data.domain.CustomerRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<CustomerRepository> { CustomerRepositoryImpl() }
}