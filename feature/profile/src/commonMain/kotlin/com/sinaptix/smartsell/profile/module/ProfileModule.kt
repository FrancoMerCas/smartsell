package com.sinaptix.smartsell.profile.module

import com.sinaptix.smartsell.profile.ProfileViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val profileModule = module {
    viewModelOf(::ProfileViewModel)
}