package com.sinaptix.smartsell.admin_panel

import ContentWithMessageBar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sinaptix.smartsell.shared.components.CustomeInfoCard
import com.sinaptix.smartsell.shared.components.CustomeLoadingCard
import com.sinaptix.smartsell.shared.domain.Order
import com.sinaptix.smartsell.shared.domain.Product
import com.sinaptix.smartsell.shared.resources.AppIcon
import com.sinaptix.smartsell.shared.resources.AppStrings
import com.sinaptix.smartsell.shared.resources.ButtonPrimary
import com.sinaptix.smartsell.shared.resources.CategoryBlue
import com.sinaptix.smartsell.shared.resources.CategoryGreen
import com.sinaptix.smartsell.shared.resources.CategoryPurple
import com.sinaptix.smartsell.shared.resources.CategoryRed
import com.sinaptix.smartsell.shared.resources.CategoryYellow
import com.sinaptix.smartsell.shared.resources.FontSize
import com.sinaptix.smartsell.shared.resources.IconPrimary
import com.sinaptix.smartsell.shared.resources.MadaBoldFont
import com.sinaptix.smartsell.shared.resources.MadaMediumFont
import com.sinaptix.smartsell.shared.resources.MadaRegularFont
import com.sinaptix.smartsell.shared.resources.MadaSemiBoldFont
import com.sinaptix.smartsell.shared.resources.MadaVariableWghtFont
import com.sinaptix.smartsell.shared.resources.SageGreen
import com.sinaptix.smartsell.shared.resources.Surface
import com.sinaptix.smartsell.shared.resources.SurfaceGreenLighter
import com.sinaptix.smartsell.shared.resources.SurfaceLighter
import com.sinaptix.smartsell.shared.resources.TabIndicator
import com.sinaptix.smartsell.shared.resources.TextPrimary
import com.sinaptix.smartsell.shared.resources.TextSecondary
import com.sinaptix.smartsell.shared.resources.TextWhite
import com.sinaptix.smartsell.shared.util.DisplayResult
import com.sinaptix.smartsell.shared.util.RequestState
import com.sinaptix.smartsell.shared.util.asStringRes
import com.sinaptix.smartsell.shared.util.formatPrice
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    navigateBack: () -> Unit,
    navigateToManage: (String?) -> Unit,
    viewModel: AdminPanelViewModel = koinViewModel()
) {
    val messageBarState = rememberMessageBarState()
    var showDeleteDialog by remember { mutableStateOf<String?>(null) }

    // Delete confirmation dialog
    if (showDeleteDialog != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = {
                Text(
                    text = "Delete Product?",
                    fontFamily = MadaSemiBoldFont(),
                    fontSize = FontSize.MEDIUM,
                    color = TextPrimary
                )
            },
            text = {
                Text(
                    text = "This action cannot be undone.",
                    fontFamily = MadaRegularFont(),
                    fontSize = FontSize.REGULAR,
                    color = TextSecondary
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog?.let { viewModel.deleteProduct(it) }
                    showDeleteDialog = null
                }) {
                    Text("Delete", color = CategoryRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }

    Scaffold(
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = AppStrings.Titles.titleAdminPanel.asStringRes(),
                        fontFamily = MadaVariableWghtFont(),
                        fontSize = FontSize.LARGE,
                        color = TextPrimary,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            painter = painterResource(AppIcon.Icon.BackArrow),
                            contentDescription = AppStrings.Descriptions.descriptIconBackArrow.asStringRes(),
                            tint = IconPrimary,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(
                            painter = painterResource(AppIcon.Icon.Search),
                            contentDescription = "Refresh",
                            tint = IconPrimary,
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
        ContentWithMessageBar(
            modifier = Modifier
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                ),
            contentBackgroundColor = Surface,
            messageBarState = messageBarState,
            errorMaxLines = 2
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Tab Row
                TabRow(
                    selectedTabIndex = viewModel.selectedTab.ordinal,
                    containerColor = SurfaceGreenLighter,
                    contentColor = TextPrimary,
                    indicator = { tabPositions ->
                        Box(
                            modifier = Modifier
                                .tabIndicatorOffset(tabPositions[viewModel.selectedTab.ordinal])
                                .height(3.dp)
                                .background(TabIndicator)
                        )
                    }
                ) {
                    AdminTab.entries.forEach { tab ->
                        Tab(
                            selected = viewModel.selectedTab == tab,
                            onClick = { viewModel.selectTab(tab) },
                            text = {
                                Text(
                                    text = tab.name,
                                    fontFamily = MadaMediumFont(),
                                    fontSize = FontSize.REGULAR,
                                    color = if (viewModel.selectedTab == tab) TextPrimary else TextSecondary
                                )
                            }
                        )
                    }
                }

                // Tab Content
                when (viewModel.selectedTab) {
                    AdminTab.Dashboard -> DashboardTab(viewModel)
                    AdminTab.Products -> ProductsTab(
                        viewModel = viewModel,
                        navigateToManage = navigateToManage,
                        onDeleteRequest = { productId -> showDeleteDialog = productId }
                    )
                    AdminTab.Orders -> OrdersTab(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
private fun DashboardTab(viewModel: AdminPanelViewModel) {
    viewModel.statsState.DisplayResult(
        modifier = Modifier.fillMaxSize(),
        onLoading = {
            CustomeLoadingCard(modifier = Modifier.fillMaxSize())
        },
        onError = { message ->
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Error: $message",
                    fontFamily = MadaRegularFont(),
                    fontSize = FontSize.REGULAR,
                    color = CategoryRed
                )
            }
        },
        onSuccess = { stats ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Overview",
                        fontFamily = MadaSemiBoldFont(),
                        fontSize = FontSize.MEDIUM,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            title = "Total Orders",
                            value = stats.totalOrders.toString(),
                            color = CategoryBlue,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Revenue",
                            value = stats.totalRevenue.formatPrice(),
                            color = CategoryGreen,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            title = "Pending",
                            value = stats.pendingOrders.toString(),
                            color = CategoryYellow,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Products",
                            value = stats.totalProducts.toString(),
                            color = SageGreen,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            title = "Low Stock",
                            value = stats.lowStockProducts.toString(),
                            color = CategoryRed,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Customers",
                            value = stats.totalCustomers.toString(),
                            color = CategoryPurple,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun ProductsTab(
    viewModel: AdminPanelViewModel,
    navigateToManage: (String?) -> Unit,
    onDeleteRequest: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Add Product button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = { navigateToManage(null) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonPrimary,
                    contentColor = TextPrimary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(AppIcon.Icon.Plus),
                    contentDescription = "Add product"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Add Product",
                    fontFamily = MadaMediumFont(),
                    fontSize = FontSize.REGULAR
                )
            }
        }

        viewModel.productsState.DisplayResult(
            modifier = Modifier.fillMaxSize(),
            onLoading = {
                CustomeLoadingCard(modifier = Modifier.fillMaxSize())
            },
            onError = { message ->
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Error: $message",
                        fontFamily = MadaRegularFont(),
                        fontSize = FontSize.REGULAR,
                        color = CategoryRed
                    )
                }
            },
            onSuccess = { products ->
                if (products.isEmpty()) {
                    CustomeInfoCard(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        image = AppIcon.Icon.Warning,
                        title = "No Products",
                        subTitle = "Add your first product to get started"
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(products) { product ->
                            AdminProductItem(
                                product = product,
                                onEdit = { navigateToManage(product.id) },
                                onDelete = { onDeleteRequest(product.id) }
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
private fun OrdersTab(viewModel: AdminPanelViewModel) {
    val orderFilters = listOf(null, "pending", "confirmed", "preparing", "ready", "shipped", "delivered")
    val filterLabels = listOf("All", "Pending", "Confirmed", "Preparing", "Ready", "Shipped", "Delivered")

    Column(modifier = Modifier.fillMaxSize()) {
        // Filter chips
        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(orderFilters.size) { index ->
                FilterChip(
                    selected = viewModel.selectedOrderFilter == orderFilters[index],
                    onClick = { viewModel.selectOrderFilter(orderFilters[index]) },
                    label = {
                        Text(
                            text = filterLabels[index],
                            fontFamily = MadaRegularFont(),
                            fontSize = FontSize.SMALL
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SurfaceGreenLighter,
                        selectedLabelColor = TextPrimary,
                        containerColor = SurfaceLighter,
                        labelColor = TextSecondary
                    )
                )
            }
        }

        viewModel.ordersState.DisplayResult(
            modifier = Modifier.fillMaxSize(),
            onLoading = {
                CustomeLoadingCard(modifier = Modifier.fillMaxSize())
            },
            onError = { message ->
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Error: $message",
                        fontFamily = MadaRegularFont(),
                        fontSize = FontSize.REGULAR,
                        color = CategoryRed
                    )
                }
            },
            onSuccess = { orders ->
                if (orders.isEmpty()) {
                    CustomeInfoCard(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        image = AppIcon.Icon.Warning,
                        title = "No Orders",
                        subTitle = "No orders found for the selected filter"
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(orders) { order ->
                            AdminOrderItem(
                                order = order,
                                onStatusUpdate = { newStatus ->
                                    viewModel.updateOrderStatus(order.id, newStatus)
                                }
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(color)
            )
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = value,
                    fontFamily = MadaBoldFont(),
                    fontSize = FontSize.EXTRA_MEDIUM,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = title,
                    fontFamily = MadaRegularFont(),
                    fontSize = FontSize.SMALL,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun AdminProductItem(
    product: Product,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceLighter),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = product.thumbnail,
                contentDescription = product.title,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.title,
                    fontFamily = MadaSemiBoldFont(),
                    fontSize = FontSize.REGULAR,
                    color = TextPrimary,
                    maxLines = 1
                )
                Text(
                    text = product.price.formatPrice(),
                    fontFamily = MadaMediumFont(),
                    fontSize = FontSize.REGULAR,
                    color = SageGreen
                )
                Text(
                    text = "Stock: ${product.stock}",
                    fontFamily = MadaRegularFont(),
                    fontSize = FontSize.SMALL,
                    color = TextSecondary
                )
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        painter = painterResource(AppIcon.Icon.Edit),
                        contentDescription = "Edit",
                        tint = SageGreen
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        painter = painterResource(AppIcon.Icon.Delete),
                        contentDescription = "Delete",
                        tint = CategoryRed
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminOrderItem(
    order: Order,
    onStatusUpdate: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceLighter),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = order.orderNumber,
                    fontFamily = MadaSemiBoldFont(),
                    fontSize = FontSize.REGULAR,
                    color = TextPrimary
                )
                StatusBadge(status = order.status)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${order.items.size} item(s) · ${order.total.formatPrice()}",
                fontFamily = MadaRegularFont(),
                fontSize = FontSize.SMALL,
                color = TextSecondary
            )
            // Action buttons - show only next valid action
            val nextStatus = when (order.status.lowercase()) {
                "pending" -> "confirmed"
                "confirmed" -> "preparing"
                "preparing" -> "ready"
                "ready" -> "delivered"
                else -> null
            }
            if (nextStatus != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { onStatusUpdate(nextStatus) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SageGreen,
                            contentColor = TextWhite
                        ),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "→ ${nextStatus.replaceFirstChar { it.uppercase() }}",
                            fontFamily = MadaMediumFont(),
                            fontSize = FontSize.SMALL
                        )
                    }
                    if (order.status.lowercase() != "cancelled") {
                        Button(
                            onClick = { onStatusUpdate("cancelled") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CategoryRed.copy(alpha = 0.1f),
                                contentColor = CategoryRed
                            ),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Cancel",
                                fontFamily = MadaMediumFont(),
                                fontSize = FontSize.SMALL
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(status: String) {
    val (bgColor, textColor) = when (status.lowercase()) {
        "pending" -> Pair(CategoryYellow.copy(alpha = 0.2f), CategoryYellow)
        "confirmed", "preparing" -> Pair(CategoryBlue.copy(alpha = 0.15f), CategoryBlue)
        "ready" -> Pair(CategoryPurple.copy(alpha = 0.15f), CategoryPurple)
        "delivered", "completed" -> Pair(CategoryGreen.copy(alpha = 0.15f), CategoryGreen)
        "cancelled" -> Pair(CategoryRed.copy(alpha = 0.15f), CategoryRed)
        else -> Pair(SurfaceLighter, TextSecondary)
    }

    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = status.uppercase(),
            fontFamily = MadaMediumFont(),
            fontSize = FontSize.EXTRA_SMALL,
            color = textColor
        )
    }
}
