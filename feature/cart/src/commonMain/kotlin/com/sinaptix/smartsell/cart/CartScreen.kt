package com.sinaptix.smartsell.cart

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sinaptix.smartsell.shared.components.CustomeErrorCard
import com.sinaptix.smartsell.shared.components.CustomeLoadingCard
import com.sinaptix.smartsell.shared.components.CustomePrimaryButton
import com.sinaptix.smartsell.shared.domain.CartItem
import com.sinaptix.smartsell.shared.resources.BorderIdle
import com.sinaptix.smartsell.shared.resources.CategoryRed
import com.sinaptix.smartsell.shared.resources.FontSize
import com.sinaptix.smartsell.shared.resources.MadaBoldFont
import com.sinaptix.smartsell.shared.resources.MadaMediumFont
import com.sinaptix.smartsell.shared.resources.MadaRegularFont
import com.sinaptix.smartsell.shared.resources.SageGreen
import com.sinaptix.smartsell.shared.resources.Surface
import com.sinaptix.smartsell.shared.resources.SurfaceLighter
import com.sinaptix.smartsell.shared.resources.SurfaceGreenLighter
import com.sinaptix.smartsell.shared.resources.TextPrimary
import com.sinaptix.smartsell.shared.resources.TextSecondary
import com.sinaptix.smartsell.shared.resources.White
import com.sinaptix.smartsell.shared.util.RequestState
import com.sinaptix.smartsell.shared.util.formatPrice
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CartScreen(
    onNavigateToCheckout: () -> Unit,
    onNavigateToProducts: () -> Unit
) {
    val viewModel = koinViewModel<CartViewModel>()

    CartContent(
        cartState = viewModel.cartState,
        subtotal = viewModel.subtotal,
        shippingCost = viewModel.shippingCost,
        tax = viewModel.tax,
        total = viewModel.total,
        onIncrementQuantity = viewModel::incrementQuantity,
        onDecrementQuantity = viewModel::decrementQuantity,
        onRemoveItem = viewModel::removeItem,
        onNavigateToCheckout = onNavigateToCheckout,
        onNavigateToProducts = onNavigateToProducts
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CartContent(
    cartState: RequestState<List<CartItem>>,
    subtotal: Double,
    shippingCost: Double,
    tax: Double,
    total: Double,
    onIncrementQuantity: (String, Int) -> Unit,
    onDecrementQuantity: (String, Int) -> Unit,
    onRemoveItem: (String) -> Unit,
    onNavigateToCheckout: () -> Unit,
    onNavigateToProducts: () -> Unit
) {
    Scaffold(
        containerColor = SurfaceLighter
    ) { paddingValues ->
        when (cartState) {
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
                        message = cartState.message
                    )
                }
            }
            is RequestState.Success -> {
                val items = cartState.data
                if (items.isEmpty()) {
                    EmptyCartContent(
                        paddingValues = paddingValues,
                        onNavigateToProducts = onNavigateToProducts
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
                        ) {
                            items(items) { item ->
                                CartItemCard(
                                    item = item,
                                    onIncrement = { onIncrementQuantity(item.id, item.quantity) },
                                    onDecrement = { onDecrementQuantity(item.id, item.quantity) },
                                    onRemove = { onRemoveItem(item.id) }
                                )
                            }
                        }
                        OrderSummary(
                            subtotal = subtotal,
                            shippingCost = shippingCost,
                            tax = tax,
                            total = total,
                            onCheckout = onNavigateToCheckout
                        )
                    }
                }
            }
            else -> {}
        }
    }
}

@Composable
private fun CartItemCard(
    item: CartItem,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.productThumbnail,
                contentDescription = item.productTitle,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.productTitle,
                    fontFamily = MadaMediumFont(),
                    fontSize = FontSize.REGULAR,
                    color = TextPrimary,
                    maxLines = 2
                )
                if (item.variantSelection.isNotEmpty()) {
                    Text(
                        text = item.variantSelection.entries.joinToString(", ") { "${it.key}: ${it.value}" },
                        fontFamily = MadaRegularFont(),
                        fontSize = FontSize.SMALL,
                        color = TextSecondary
                    )
                }
                Text(
                    text = item.priceSnapshot.formatPrice(),
                    fontFamily = MadaBoldFont(),
                    fontSize = FontSize.REGULAR,
                    color = SageGreen,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .background(SurfaceLighter, RoundedCornerShape(6.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "−",
                            fontSize = FontSize.MEDIUM,
                            color = TextPrimary,
                            modifier = Modifier.clickable { onDecrement() }
                        )
                        Text(
                            text = item.quantity.toString(),
                            fontFamily = MadaBoldFont(),
                            fontSize = FontSize.REGULAR,
                            color = TextPrimary
                        )
                        Text(
                            text = "+",
                            fontSize = FontSize.MEDIUM,
                            color = TextPrimary,
                            modifier = Modifier.clickable { onIncrement() }
                        )
                    }
                    Text(
                        text = "Remove",
                        fontFamily = MadaRegularFont(),
                        fontSize = FontSize.SMALL,
                        color = CategoryRed,
                        modifier = Modifier.clickable { onRemove() }
                    )
                }
            }
        }
    }
}

@Composable
private fun OrderSummary(
    subtotal: Double,
    shippingCost: Double,
    tax: Double,
    total: Double,
    onCheckout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Surface)
            .padding(16.dp)
    ) {
        SummaryRow(label = "Subtotal", value = subtotal)
        SummaryRow(
            label = "Shipping",
            value = shippingCost,
            valueColor = if (shippingCost == 0.0) com.sinaptix.smartsell.shared.resources.CategoryGreen else TextSecondary
        )
        SummaryRow(label = "Tax", value = tax)
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = BorderIdle
        )
        SummaryRow(
            label = "Total",
            value = total,
            labelStyle = FontSize.EXTRA_REGULAR,
            valueColor = TextPrimary
        )
        Spacer(modifier = Modifier.height(16.dp))
        CustomePrimaryButton(
            text = "Proceed to Checkout",
            onClick = onCheckout
        )
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: Double,
    labelStyle: androidx.compose.ui.unit.TextUnit = FontSize.REGULAR,
    valueColor: androidx.compose.ui.graphics.Color = TextSecondary
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontFamily = MadaRegularFont(),
            fontSize = labelStyle,
            color = TextPrimary
        )
        Text(
            text = value.formatPrice(),
            fontFamily = MadaBoldFont(),
            fontSize = labelStyle,
            color = valueColor
        )
    }
}

@Composable
private fun EmptyCartContent(
    paddingValues: androidx.compose.foundation.layout.PaddingValues,
    onNavigateToProducts: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your cart is empty",
            fontFamily = MadaBoldFont(),
            fontSize = FontSize.MEDIUM,
            color = TextPrimary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Add some products to get started",
            fontFamily = MadaRegularFont(),
            fontSize = FontSize.REGULAR,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(24.dp))
        CustomePrimaryButton(
            text = "Go Shopping",
            onClick = onNavigateToProducts
        )
    }
}