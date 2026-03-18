package com.sinaptix.smartsell.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.sinaptix.smartsell.admin_panel.AdminPanelScreen
import com.sinaptix.smartsell.auth.AuthScreen
import com.sinaptix.smartsell.cart.CartScreen
import com.sinaptix.smartsell.home.CategoriesScreen
import com.sinaptix.smartsell.home.HomeGraphScreen
import com.sinaptix.smartsell.manage_product.ManageProductScreen
import com.sinaptix.smartsell.orders.CheckoutScreen
import com.sinaptix.smartsell.orders.OrderDetailScreen
import com.sinaptix.smartsell.orders.OrderHistoryScreen
import com.sinaptix.smartsell.products.ProductDetailScreen
import com.sinaptix.smartsell.products.ProductsOverviewScreen
import com.sinaptix.smartsell.profile.ProfileScreen
import com.sinaptix.smartsell.shared.navigation.Screen


@Composable
fun SetupNavGraph(
    startDestination: Screen = Screen.Auth
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Screen.Auth> {
            AuthScreen(
                navigateToHome = {
                    navController.navigate(Screen.HomeGraph) {
                        popUpTo<Screen.Auth> { inclusive = true }
                    }
                }
            )
        }
        composable<Screen.HomeGraph> {
            HomeGraphScreen(
                navigateToAuth = {
                    navController.navigate(Screen.Auth) {
                        popUpTo<Screen.HomeGraph> { inclusive = true }
                    }
                },
                navigateToProfile = {
                    navController.navigate(Screen.Profile)
                },
                navigateToAdminPanel = {
                    navController.navigate(Screen.AdminPanel)
                },
                navigateToProductDetail = { productId ->
                    navController.navigate(Screen.ProductDetail(productId))
                },
                navigateToCheckout = {
                    navController.navigate(Screen.Checkout)
                },
                productsOverviewContent = {
                    ProductsOverviewScreen(
                        onNavigateToProductDetail = { productId ->
                            navController.navigate(Screen.ProductDetail(productId))
                        }
                    )
                },
                cartContent = {
                    CartScreen(
                        onNavigateToCheckout = {
                            navController.navigate(Screen.Checkout)
                        },
                        onNavigateToProducts = {
                            navController.navigate(Screen.ProductsOverview) {
                                launchSingleTop = true
                            }
                        }
                    )
                },
                categoriesContent = {
                    CategoriesScreen(
                        onNavigateToProducts = { categoryId ->
                            navController.navigate(Screen.ProductsOverview)
                        }
                    )
                }
            )
        }
        composable<Screen.Profile> {
            ProfileScreen(
                navigateBack = {
                    navController.navigateUp()
                }
            )
        }
        composable<Screen.AdminPanel> {
            AdminPanelScreen(
                navigateBack = {
                    navController.navigateUp()
                },
                navigateToManage = { id ->
                    navController.navigate(Screen.ManageProduct(id = id))
                }
            )
        }
        composable<Screen.ManageProduct> {
            val id = it.toRoute<Screen.ManageProduct>().id
            ManageProductScreen(
                id = id,
                navigateBack = {
                    navController.navigateUp()
                }
            )
        }
        composable<Screen.ProductDetail> {
            val productId = it.toRoute<Screen.ProductDetail>().productId
            ProductDetailScreen(
                productId = productId,
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToCart = {
                    navController.navigate(Screen.Cart)
                }
            )
        }
        composable<Screen.Checkout> {
            CheckoutScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onOrderPlaced = { orderId ->
                    navController.navigate(Screen.OrderDetail(orderId)) {
                        popUpTo<Screen.Checkout> { inclusive = true }
                    }
                }
            )
        }
        composable<Screen.OrderHistory> {
            OrderHistoryScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToOrderDetail = { orderId ->
                    navController.navigate(Screen.OrderDetail(orderId))
                }
            )
        }
        composable<Screen.OrderDetail> {
            val orderId = it.toRoute<Screen.OrderDetail>().orderId
            OrderDetailScreen(
                orderId = orderId,
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}
