package com.sinaptix.smartsell

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import com.sinaptix.smartsell.data.domain.AuthRepository
import com.sinaptix.smartsell.shared.navigation.Screen
import com.sinaptix.smartsell.navigation.SetupNavGraph
import com.sinaptix.smartsell.shared.Constants.WEB_CLIENT_ID
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    MaterialTheme {
        val authRepository = koinInject<AuthRepository>()
        var appReady by remember { mutableStateOf(false) }
        val isUserAuthenticated = remember { authRepository.isLoggedIn() }
        val startDestination = remember {
            if (isUserAuthenticated) {
                Screen.HomeGraph
            } else {
                Screen.Auth
            }
        }

        LaunchedEffect(Unit) {
            try {
                GoogleAuthProvider.create(
                    credentials = GoogleAuthCredentials(
                        serverId = WEB_CLIENT_ID
                    )
                )
            } catch (e: Exception) {
                // GoogleAuthProvider may fail on some platforms — app continues regardless
            } finally {
                appReady = true
            }
        }

        if (appReady) {
            SetupNavGraph(
                startDestination = startDestination
            )
        }
    }
}