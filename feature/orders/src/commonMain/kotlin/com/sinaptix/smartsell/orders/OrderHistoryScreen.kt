package com.sinaptix.smartsell.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sinaptix.smartsell.shared.components.CustomeErrorCard
import com.sinaptix.smartsell.shared.components.CustomeLoadingCard
import com.sinaptix.smartsell.shared.domain.Order
import com.sinaptix.smartsell.shared.resources.CategoryBlue
import com.sinaptix.smartsell.shared.resources.CategoryGreen
import com.sinaptix.smartsell.shared.resources.CategoryPurple
import com.sinaptix.smartsell.shared.resources.CategoryRed
import com.sinaptix.smartsell.shared.resources.CategoryYellow
import com.sinaptix.smartsell.shared.resources.FontSize
import com.sinaptix.smartsell.shared.resources.MadaBoldFont
import com.sinaptix.smartsell.shared.resources.MadaMediumFont
import com.sinaptix.smartsell.shared.resources.MadaRegularFont
import com.sinaptix.smartsell.shared.resources.Surface
import com.sinaptix.smartsell.shared.resources.SurfaceGreenLighter
import com.sinaptix.smartsell.shared.resources.SurfaceLighter
import com.sinaptix.smartsell.shared.resources.TextPrimary
import com.sinaptix.smartsell.shared.resources.TextSecondary
import com.sinaptix.smartsell.shared.resources.White
import com.sinaptix.smartsell.shared.util.RequestState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OrderHistoryScreen(
    onNavigateBack: () -> Unit,
    onNavigateToOrderDetail: (String) -> Unit
) {
    val viewModel = koinViewModel<OrderHistoryViewModel>()

    OrderHistoryContent(
        ordersState = viewModel.ordersState,
        onNavigateBack = onNavigateBack,
        onNavigateToOrderDetail = onNavigateToOrderDetail,
        onRetry = viewModel::refresh
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OrderHistoryContent(
    ordersState: RequestState<List<Order>>,
    onNavigateBack: () -> Unit,
    onNavigateToOrderDetail: (String) -> Unit,
    onRetry: () -> Unit
) {
    Scaffold(
        containerColor = SurfaceLighter,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Orders",
                        fontFamily = MadaBoldFont(),
                        fontSize = FontSize.EXTRA_REGULAR,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text(text = "←", fontSize = FontSize.LARGE, color = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SurfaceGreenLighter
                )
            )
        }
    ) { paddingValues ->
        when (ordersState) {
            is RequestState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CustomeLoadingCard()
                }
            }
            is RequestState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CustomeErrorCard(
                        modifier = Modifier.padding(16.dp),
                        message = ordersState.message
                    )
                }
            }
            is RequestState.Success -> {
                val orders = ordersState.data
                if (orders.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "No orders yet",
                                fontFamily = MadaBoldFont(),
                                fontSize = FontSize.MEDIUM,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Your orders will appear here",
                                fontFamily = MadaRegularFont(),
                                fontSize = FontSize.REGULAR,
                                color = TextSecondary
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(orders) { order ->
                            OrderCard(
                                order = order,
                                onClick = { onNavigateToOrderDetail(order.id) }
                            )
                        }
                    }
                }
            }
            else -> {}
        }
    }
}

@Composable
private fun OrderCard(
    order: Order,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = order.orderNumber,
                    fontFamily = MadaBoldFont(),
                    fontSize = FontSize.REGULAR,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${order.items.size} items · ${"$%.2f".format(order.total)}",
                    fontFamily = MadaRegularFont(),
                    fontSize = FontSize.REGULAR,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                if (order.createdAt.isNotBlank()) {
                    Text(
                        text = formatOrderDate(order.createdAt),
                        fontFamily = MadaRegularFont(),
                        fontSize = FontSize.SMALL,
                        color = TextSecondary
                    )
                }
            }
            StatusBadge(status = order.status)
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val (color, label) = statusInfo(status)
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontFamily = MadaMediumFont(),
            fontSize = FontSize.EXTRA_SMALL,
            color = color,
            fontWeight = FontWeight.SemiBold
        )
    }
}

fun statusInfo(status: String): Pair<Color, String> {
    return when (status.lowercase()) {
        "pending" -> Pair(CategoryYellow, "PENDING")
        "confirmed", "preparing" -> Pair(CategoryBlue, status.uppercase())
        "ready", "shipped" -> Pair(CategoryPurple, status.uppercase())
        "delivered", "completed" -> Pair(CategoryGreen, status.uppercase())
        "cancelled" -> Pair(CategoryRed, "CANCELLED")
        else -> Pair(TextSecondary, status.uppercase())
    }
}

private fun formatOrderDate(dateString: String): String {
    return try {
        // Basic formatting - just return first 10 chars of ISO date
        if (dateString.length >= 10) dateString.substring(0, 10) else dateString
    } catch (e: Exception) {
        dateString
    }
}
