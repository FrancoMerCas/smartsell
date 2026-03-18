package com.sinaptix.smartsell.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sinaptix.smartsell.shared.components.CustomeLoadingCard
import com.sinaptix.smartsell.shared.components.CustomePrimaryButton
import com.sinaptix.smartsell.shared.components.CustomeTextField
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
fun CheckoutScreen(
    onNavigateBack: () -> Unit,
    onOrderPlaced: (String) -> Unit
) {
    val viewModel = koinViewModel<CheckoutViewModel>()

    CheckoutContent(
        fulfillmentType = viewModel.fulfillmentType,
        firstName = viewModel.firstName,
        lastName = viewModel.lastName,
        address = viewModel.address,
        city = viewModel.city,
        zip = viewModel.zip,
        phone = viewModel.phone,
        notes = viewModel.notes,
        subtotal = viewModel.subtotal,
        shippingCost = viewModel.shippingCost,
        tax = viewModel.tax,
        total = viewModel.total,
        itemCount = viewModel.cartItems.size,
        isFormValid = viewModel.isFormValid,
        placeOrderState = viewModel.placeOrderState,
        onNavigateBack = onNavigateBack,
        onFulfillmentTypeChanged = { viewModel.fulfillmentType = it },
        onFirstNameChanged = { viewModel.firstName = it },
        onLastNameChanged = { viewModel.lastName = it },
        onAddressChanged = { viewModel.address = it },
        onCityChanged = { viewModel.city = it },
        onZipChanged = { viewModel.zip = it },
        onPhoneChanged = { viewModel.phone = it },
        onNotesChanged = { viewModel.notes = it },
        onPlaceOrder = {
            viewModel.placeOrder(
                storeId = "default",
                onSuccess = onOrderPlaced,
                onError = {}
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CheckoutContent(
    fulfillmentType: String,
    firstName: String,
    lastName: String,
    address: String,
    city: String,
    zip: String,
    phone: String,
    notes: String,
    subtotal: Double,
    shippingCost: Double,
    tax: Double,
    total: Double,
    itemCount: Int,
    isFormValid: Boolean,
    placeOrderState: RequestState<*>,
    onNavigateBack: () -> Unit,
    onFulfillmentTypeChanged: (String) -> Unit,
    onFirstNameChanged: (String) -> Unit,
    onLastNameChanged: (String) -> Unit,
    onAddressChanged: (String) -> Unit,
    onCityChanged: (String) -> Unit,
    onZipChanged: (String) -> Unit,
    onPhoneChanged: (String) -> Unit,
    onNotesChanged: (String) -> Unit,
    onPlaceOrder: () -> Unit
) {
    Scaffold(
        containerColor = SurfaceLighter,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Checkout",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Fulfillment Type
            SectionCard {
                Text(
                    text = "Fulfillment Type",
                    fontFamily = MadaSemiBoldFont(),
                    fontSize = FontSize.REGULAR,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    FulfillmentOption(
                        label = "Delivery",
                        value = "delivery",
                        selected = fulfillmentType == "delivery",
                        onSelect = onFulfillmentTypeChanged
                    )
                    FulfillmentOption(
                        label = "Pickup",
                        value = "pickup",
                        selected = fulfillmentType == "pickup",
                        onSelect = onFulfillmentTypeChanged
                    )
                }
            }

            // Shipping Address (only for delivery)
            if (fulfillmentType == "delivery") {
                SectionCard {
                    Text(
                        text = "Shipping Address",
                        fontFamily = MadaSemiBoldFont(),
                        fontSize = FontSize.REGULAR,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        CustomeTextField(
                            modifier = Modifier.weight(1f),
                            value = firstName,
                            onValueChange = onFirstNameChanged,
                            placeHolder = "First Name"
                        )
                        CustomeTextField(
                            modifier = Modifier.weight(1f),
                            value = lastName,
                            onValueChange = onLastNameChanged,
                            placeHolder = "Last Name"
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomeTextField(
                        value = address,
                        onValueChange = onAddressChanged,
                        placeHolder = "Address"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        CustomeTextField(
                            modifier = Modifier.weight(1f),
                            value = city,
                            onValueChange = onCityChanged,
                            placeHolder = "City"
                        )
                        CustomeTextField(
                            modifier = Modifier.weight(1f),
                            value = zip,
                            onValueChange = onZipChanged,
                            placeHolder = "ZIP"
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomeTextField(
                        value = phone,
                        onValueChange = onPhoneChanged,
                        placeHolder = "Phone"
                    )
                }
            }

            // Notes
            SectionCard {
                CustomeTextField(
                    value = notes,
                    onValueChange = onNotesChanged,
                    placeHolder = "Notes (optional)"
                )
            }

            // Order Summary
            SectionCard {
                Text(
                    text = "Order Summary",
                    fontFamily = MadaSemiBoldFont(),
                    fontSize = FontSize.REGULAR,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(12.dp))
                SummaryRow(label = "$itemCount items", value = subtotal)
                SummaryRow(
                    label = "Shipping",
                    value = shippingCost,
                    valueColor = if (shippingCost == 0.0) CategoryGreen else TextSecondary,
                    valueText = if (shippingCost == 0.0) "Free" else null
                )
                SummaryRow(label = "Tax (16%)", value = tax)
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
            }

            when (placeOrderState) {
                is RequestState.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CustomeLoadingCard()
                    }
                }
                is RequestState.Error -> {
                    Text(
                        text = (placeOrderState as RequestState.Error).message,
                        fontFamily = MadaRegularFont(),
                        fontSize = FontSize.SMALL,
                        color = com.sinaptix.smartsell.shared.resources.CategoryRed
                    )
                }
                else -> {}
            }

            CustomePrimaryButton(
                text = "Place Order",
                enabled = isFormValid && placeOrderState !is RequestState.Loading,
                onClick = onPlaceOrder
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SectionCard(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Surface, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        content()
    }
}

@Composable
private fun FulfillmentOption(
    label: String,
    value: String,
    selected: Boolean,
    onSelect: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.clickable { onSelect(value) }
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .border(2.dp, if (selected) SageGreen else BorderIdle, CircleShape)
                .background(if (selected) SageGreen else Surface),
            contentAlignment = Alignment.Center
        ) {
            if (selected) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Surface)
                )
            }
        }
        Text(
            text = label,
            fontFamily = MadaMediumFont(),
            fontSize = FontSize.REGULAR,
            color = TextPrimary
        )
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: Double,
    labelStyle: androidx.compose.ui.unit.TextUnit = FontSize.REGULAR,
    valueColor: androidx.compose.ui.graphics.Color = TextSecondary,
    valueText: String? = null
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
            text = valueText ?: "$%.2f".format(value),
            fontFamily = MadaBoldFont(),
            fontSize = labelStyle,
            color = valueColor
        )
    }
}
