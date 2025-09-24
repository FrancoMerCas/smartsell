package com.sinaptix.smartsell.di

import com.sinaptix.smartsell.auth.module.authModule
import com.sinaptix.smartsell.data.module.repositoryModule
import com.sinaptix.smartsell.home.module.homeModule
import com.sinaptix.smartsell.profile.module.profileModule
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
    authModule,
    homeModule,
    profileModule
)