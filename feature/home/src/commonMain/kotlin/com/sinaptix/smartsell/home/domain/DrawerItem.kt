package com.sinaptix.smartsell.home.domain

import com.sinaptix.smartsell.shared.resources.AppIcon
import org.jetbrains.compose.resources.DrawableResource

enum class DrawerItem(
    val icon: DrawableResource
) {
    Profile(icon = AppIcon.Icon.Person),
    Blog(icon = AppIcon.Icon.Book),
    Location(icon = AppIcon.Icon.MapPin),
    Contact(icon = AppIcon.Icon.Edit),
    SignOut(icon = AppIcon.Icon.SignOut),
    Admin(icon = AppIcon.Icon.Unlock),
}