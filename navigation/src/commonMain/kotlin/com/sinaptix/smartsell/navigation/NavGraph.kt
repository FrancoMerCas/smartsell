package com.sinaptix.smartsell.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sinaptix.smartsell.auth.AuthScreen


@Composable
fun SetupNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Auth
    ) {
        composable<Screen.Auth> {
            AuthScreen()
        }
    }
}