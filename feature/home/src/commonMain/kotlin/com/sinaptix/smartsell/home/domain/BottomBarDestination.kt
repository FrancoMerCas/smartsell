package com.sinaptix.smartsell.home.domain

import com.sinaptix.smartsell.shared.Resources
import com.sinaptix.smartsell.shared.navigation.Screen
import org.jetbrains.compose.resources.DrawableResource

enum class BottomBarDestination(
    val icon: DrawableResource,
    val title: String,
    val screen: Screen
) {
    ProductsOverview(
        icon = Resources.Icon.Home,
        title = "SmartSell",
        screen = Screen.ProductsOverview
    ),
    Cart(
    icon = Resources.Icon.ShoppingCart,
    title = "Cart",
    screen = Screen.Cart
    ),
    Categories(
    icon = Resources.Icon.Categories,
    title = "Categories",
    screen = Screen.Categories
    )
}