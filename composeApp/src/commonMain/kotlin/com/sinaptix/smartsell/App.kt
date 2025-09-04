package com.sinaptix.smartsell

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.sinaptix.smartsell.navigation.SetupNavGraph
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        SetupNavGraph()
    }
}