package com.sinaptix.smartsell

import androidx.compose.ui.window.ComposeUIViewController
import com.sinaptix.smartsell.di.initializeKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initializeKoin() }
) { App() }