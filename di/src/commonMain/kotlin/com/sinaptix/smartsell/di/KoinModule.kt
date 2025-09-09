package com.sinaptix.smartsell.di

import com.sinaptix.smartsell.auth.module.authModule
import com.sinaptix.smartsell.data.module.repositoryModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

fun initializeKoin(
    config: (KoinApplication.() ->Unit)? = null
) {
    startKoin{
        config?.invoke(this)
        modules(
            getAllModules()
        )
    }
}

private fun getAllModules() = listOf(
    repositoryModule,
    authModule
)