package com.sinaptix.smartsell.home.domain

import androidx.compose.runtime.Composable
import com.sinaptix.smartsell.shared.resources.AppStrings
import org.jetbrains.compose.resources.stringResource

@Composable
fun BottomBarDestination.getTitle(): String {
    return when (this) {
        BottomBarDestination.ProductsOverview -> stringResource(AppStrings.AppName.appName)
        BottomBarDestination.Cart -> stringResource(AppStrings.Titles.titleCart)
        BottomBarDestination.Categories -> stringResource(AppStrings.Titles.titleCategorie)
    }
}