package com.sinaptix.smartsell.admin_panel.module

import com.sinaptix.smartsell.admin_panel.AdminPanelViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val adminPanelModule = module {
    viewModelOf(::AdminPanelViewModel)
}
