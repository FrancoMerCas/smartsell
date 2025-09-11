package com.sinaptix.smartsell.home.domain

import androidx.compose.runtime.Composable
import com.sinaptix.smartsell.shared.resources.AppStrings
import org.jetbrains.compose.resources.stringResource

@Composable
fun DrawerItem.getTitle(): String {
    return when (this) {
        DrawerItem.Profile -> stringResource(AppStrings.Navigation.profile)
        DrawerItem.Blog -> stringResource(AppStrings.Navigation.blog)
        DrawerItem.Location -> stringResource(AppStrings.Navigation.location)
        DrawerItem.Contact -> stringResource(AppStrings.Navigation.contact)
        DrawerItem.SignOut -> stringResource(AppStrings.Navigation.signOut)
        DrawerItem.Admin -> stringResource(AppStrings.Navigation.adminPanel)
    }
}