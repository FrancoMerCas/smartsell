package com.sinaptix.smartsell.home.domain

import com.sinaptix.smartsell.shared.navigation.Screen
import com.sinaptix.smartsell.shared.resources.AppIcon
import org.jetbrains.compose.resources.DrawableResource

enum class BottomBarDestination(
    val icon: DrawableResource,
    val title: String,
    val screen: Screen
) {
    ProductsOverview(
        icon = AppIcon.Icon.Home,
        title = "SmartSell",
        screen = Screen.ProductsOverview
    ),
    Cart(
    icon = AppIcon.Icon.ShoppingCart,
    title = "Cart",
    screen = Screen.Cart
    ),
    Categories(
    icon = AppIcon.Icon.Categories,
    title = "Categories",
    screen = Screen.Categories
    )
}