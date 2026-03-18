package com.sinaptix.smartsell.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sinaptix.smartsell.shared.components.CustomeErrorCard
import com.sinaptix.smartsell.shared.components.CustomeLoadingCard
import com.sinaptix.smartsell.shared.domain.Order
import com.sinaptix.smartsell.shared.domain.OrderItem
import com.sinaptix.smartsell.shared.resources.BorderIdle
import com.sinaptix.smartsell.shared.resources.CategoryGreen
import com.sinaptix.smartsell.shared.resources.FontSize
import com.sinaptix.smartsell.shared.resources.MadaBoldFont
import com.sinaptix.smartsell.shared.resources.MadaMediumFont
import com.sinaptix.smartsell.shared.resources.MadaRegularFont
import com.sinaptix.smartsell.shared.resources.MadaSemiBoldFont
import com.sinaptix.smartsell.shared.resources.SageGreen
import com.sinaptix.smartsell.shared.resources.Surface
import com.sinaptix.smartsell.shared.resources.SurfaceGreenLighter
import com.sinaptix.smartsell.shared.resources.SurfaceLighter
import com.sinaptix.smartsell.shared.resources.TextPrimary
import com.sinaptix.smartsell.shared.resources.TextSecondary
import com.sinaptix.smartsell.shared.util.RequestState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OrderDetailScreen(
    orderId: String,
    onNavigateBack: () -> Unit
) {
    val viewModel = koinViewModel<OrderDetailViewModel>()

    LaunchedEffect(orderId) {
        viewModel.loadOrder(orderId)
    }

    OrderDetailContent(
        orderState = viewModel.orderState,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OrderDetailContent(
    orderState: RequestState<Order>,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        containerColor = SurfaceLighter,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Order Detail",
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
        when (orderState) {
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
                        message = orderState.message
                    )
                }
            }
            is RequestState.Success -> {
                val order = orderState.data
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        OrderHeaderCard(order = order)
                    }
                    item {
                        Text(
                            text = "Items",
                            fontFamily = MadaBoldFont(),
                            fontSize = FontSize.EXTRA_REGULAR,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    items(order.items) { item ->
                        OrderItemRow(item = item)
                    }
                    item {
                        PriceSummaryCard(order = order)
                    }
                }
            }
            else -> {}
        }
    }
}

@Composable
private fun OrderHeaderCard(order: Order) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = order.orderNumber,
                    fontFamily = MadaBoldFont(),
                    fontSize = FontSize.MEDIUM,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                StatusBadge(status = order.status)
            }
            Spacer(modifier = Modifier.height(8.dp))
            DetailRow(label = "Fulfillment", value = order.fulfillmentType.replaceFirstChar { it.uppercase() })
            DetailRow(label = "Payment", value = order.paymentStatus.replaceFirstChar { it.uppercase() })
            if (order.createdAt.isNotBlank()) {
                DetailRow(label = "Date", value = formatOrderDate(order.createdAt))
            }
            if (!order.notes.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Notes: ${order.notes}",
                    fontFamily = MadaRegularFont(),
                    fontSize = FontSize.SMALL,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontFamily = MadaRegularFont(),
            fontSize = FontSize.REGULAR,
            color = TextSecondary
        )
        Text(
            text = value,
            fontFamily = MadaMediumFont(),
            fontSize = FontSize.REGULAR,
            color = TextPrimary
        )
    }
}

@Composable
private fun OrderItemRow(item: OrderItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.thumbnail,
                contentDescription = item.title,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    fontFamily = MadaMediumFont(),
                    fontSize = FontSize.REGULAR,
                    color = TextPrimary
                )
                if (item.variantSelection.isNotEmpty()) {
                    Text(
                        text = item.variantSelection.entries.joinToString(", ") { "${it.key}: ${it.value}" },
                        fontFamily = MadaRegularFont(),
                        fontSize = FontSize.SMALL,
                        color = TextSecondary
                    )
                }
                if (!item.specialInstructions.isNullOrBlank()) {
                    Text(
                        text = "Note: ${item.specialInstructions}",
                        fontFamily = MadaRegularFont(),
                        fontSize = FontSize.SMALL,
                        color = TextSecondary
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "×${item.quantity}",
                    fontFamily = MadaRegularFont(),
                    fontSize = FontSize.SMALL,
                    color = TextSecondary
                )
                Text(
                    text = "$%.2f".format(item.unitPrice * item.quantity),
                    fontFamily = MadaBoldFont(),
                    fontSize = FontSize.REGULAR,
                    color = SageGreen
                )
            }
        }
    }
}

@Composable
private fun PriceSummaryCard(order: Order) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Summary",
                fontFamily = MadaSemiBoldFont(),
                fontSize = FontSize.REGULAR,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            PriceLine("Subtotal", order.subtotal)
            PriceLine("Shipping", order.shippingCost, if (order.shippingCost == 0.0) CategoryGreen else TextSecondary)
            PriceLine("Tax", order.tax)
            if (order.discount > 0) {
                PriceLine("Discount", -order.discount, CategoryGreen)
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = BorderIdle)
            PriceLine("Total", order.total, TextPrimary, fontSize = FontSize.EXTRA_REGULAR)
        }
    }
}

@Composable
private fun PriceLine(
    label: String,
    value: Double,
    valueColor: androidx.compose.ui.graphics.Color = TextSecondary,
    fontSize: androidx.compose.ui.unit.TextUnit = FontSize.REGULAR
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontFamily = MadaRegularFont(),
            fontSize = fontSize,
            color = TextPrimary
        )
        Text(
            text = "$%.2f".format(value),
            fontFamily = MadaBoldFont(),
            fontSize = fontSize,
            color = valueColor
        )
    }
}

private fun formatOrderDate(dateString: String): String {
    return if (dateString.length >= 10) dateString.substring(0, 10) else dateString
}
