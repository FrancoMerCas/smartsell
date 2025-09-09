package com.sinaptix.smartsell.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sinaptix.smartsell.home.component.BottomBar
import com.sinaptix.smartsell.home.domain.BottomBarDestination
import com.sinaptix.smartsell.shared.FontSize
import com.sinaptix.smartsell.shared.IconPrimary
import com.sinaptix.smartsell.shared.MadaVariableWghtFont
import com.sinaptix.smartsell.shared.Resources
import com.sinaptix.smartsell.shared.Surface
import com.sinaptix.smartsell.shared.SurfaceGreenLighter
import com.sinaptix.smartsell.shared.TextPrimary
import com.sinaptix.smartsell.shared.navigation.Screen

import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeGraphScreen() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState()
    val selectedDestination by remember {
        derivedStateOf {
            val route = currentRoute.value?.destination?.route.toString()

            when {
                route.contains(BottomBarDestination.ProductsOverview.screen.toString()) -> BottomBarDestination.ProductsOverview
                route.contains(BottomBarDestination.Cart.screen.toString()) -> BottomBarDestination.Cart
                route.contains(BottomBarDestination.Categories.screen.toString()) -> BottomBarDestination.Categories
                else -> BottomBarDestination.ProductsOverview
            }
        }
    }
    Scaffold(
        containerColor = Surface,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    AnimatedContent(
                        targetState = selectedDestination
                    ) { destination ->
                        Text(
                            text = destination.title,
                            fontFamily = MadaVariableWghtFont(),
                            fontSize = FontSize.LARGE,
                            color = TextPrimary,
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(Resources.Icon.Menu),
                            contentDescription = "Menu Icon",
                            tint = IconPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = SurfaceGreenLighter,
                    scrolledContainerColor = SurfaceGreenLighter,
                    navigationIconContentColor = IconPrimary,
                    titleContentColor = TextPrimary,
                    actionIconContentColor = IconPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                )
        ) {
            NavHost(
                modifier = Modifier.weight(1f),
                navController = navController,
                startDestination = Screen.ProductsOverview
            ) {
                composable<Screen.ProductsOverview> {}
                composable<Screen.Cart> {}
                composable<Screen.Categories> {}
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .padding(all = 12.dp)
            ) {
                BottomBar(
                    selected = selectedDestination,
                    onSelect = { destination ->
                        navController.navigate(destination.screen) {
                            launchSingleTop = true
                            popUpTo<Screen.ProductsOverview>() {
                                saveState = true
                                inclusive = false
                            }
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}