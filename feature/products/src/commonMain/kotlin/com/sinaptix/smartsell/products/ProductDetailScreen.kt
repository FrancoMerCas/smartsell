package com.sinaptix.smartsell.products

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sinaptix.smartsell.shared.components.CustomeErrorCard
import com.sinaptix.smartsell.shared.components.CustomeLoadingCard
import com.sinaptix.smartsell.shared.components.CustomePrimaryButton
import com.sinaptix.smartsell.shared.domain.Product
import com.sinaptix.smartsell.shared.resources.BorderIdle
import com.sinaptix.smartsell.shared.resources.CategoryRed
import com.sinaptix.smartsell.shared.resources.FilterChipSelected
import com.sinaptix.smartsell.shared.resources.FilterChipUnselected
import com.sinaptix.smartsell.shared.resources.FontSize
import com.sinaptix.smartsell.shared.resources.InputPlaceholder
import com.sinaptix.smartsell.shared.resources.MadaBoldFont
import com.sinaptix.smartsell.shared.resources.MadaMediumFont
import com.sinaptix.smartsell.shared.resources.MadaRegularFont
import com.sinaptix.smartsell.shared.resources.MadaSemiBoldFont
import com.sinaptix.smartsell.shared.resources.PriceOriginal
import com.sinaptix.smartsell.shared.resources.SageGreen
import com.sinaptix.smartsell.shared.resources.StarFilled
import com.sinaptix.smartsell.shared.resources.Surface
import com.sinaptix.smartsell.shared.resources.SurfaceGreenLighter
import com.sinaptix.smartsell.shared.resources.TextPrimary
import com.sinaptix.smartsell.shared.resources.TextSecondary
import com.sinaptix.smartsell.shared.util.RequestState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProductDetailScreen(
    productId: String,
    onNavigateBack: () -> Unit,
    onNavigateToCart: () -> Unit
) {
    val viewModel = koinViewModel<ProductDetailViewModel>()

    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    ProductDetailContent(
        productState = viewModel.productState,
        selectedVariants = viewModel.selectedVariants,
        quantity = viewModel.quantity,
        specialInstructions = viewModel.specialInstructions,
        currentPrice = viewModel.currentPrice,
        isInStock = viewModel.isInStock,
        addToCartState = viewModel.addToCartState,
        onNavigateBack = onNavigateBack,
        onSelectVariant = viewModel::selectVariant,
        onIncrementQuantity = viewModel::incrementQuantity,
        onDecrementQuantity = viewModel::decrementQuantity,
        onSpecialInstructionsChanged = { viewModel.specialInstructions = it },
        onAddToCart = {
            viewModel.addToCart(
                storeId = "default",
                onSuccess = { onNavigateToCart() },
                onError = { /* error handled via addToCartState */ }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductDetailContent(
    productState: RequestState<Product>,
    selectedVariants: Map<String, String>,
    quantity: Int,
    specialInstructions: String,
    currentPrice: Double,
    isInStock: Boolean,
    addToCartState: RequestState<Unit>,
    onNavigateBack: () -> Unit,
    onSelectVariant: (String, String) -> Unit,
    onIncrementQuantity: () -> Unit,
    onDecrementQuantity: () -> Unit,
    onSpecialInstructionsChanged: (String) -> Unit,
    onAddToCart: () -> Unit
) {
    Scaffold(
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text(text = "←", fontSize = FontSize.LARGE, color = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Surface
                )
            )
        }
    ) { paddingValues ->
        when (productState) {
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
                        message = productState.message
                    )
                }
            }
            is RequestState.Success -> {
                val product = productState.data
                ProductDetailBody(
                    product = product,
                    selectedVariants = selectedVariants,
                    quantity = quantity,
                    specialInstructions = specialInstructions,
                    currentPrice = currentPrice,
                    isInStock = isInStock,
                    isAddingToCart = addToCartState is RequestState.Loading,
                    paddingValues = paddingValues,
                    onSelectVariant = onSelectVariant,
                    onIncrementQuantity = onIncrementQuantity,
                    onDecrementQuantity = onDecrementQuantity,
                    onSpecialInstructionsChanged = onSpecialInstructionsChanged,
                    onAddToCart = onAddToCart
                )
            }
            else -> {}
        }
    }
}

@Composable
private fun ProductDetailBody(
    product: Product,
    selectedVariants: Map<String, String>,
    quantity: Int,
    specialInstructions: String,
    currentPrice: Double,
    isInStock: Boolean,
    isAddingToCart: Boolean,
    paddingValues: androidx.compose.foundation.layout.PaddingValues,
    onSelectVariant: (String, String) -> Unit,
    onIncrementQuantity: () -> Unit,
    onDecrementQuantity: () -> Unit,
    onSpecialInstructionsChanged: (String) -> Unit,
    onAddToCart: () -> Unit
) {
    val scrollState = rememberScrollState()
    val images = if (product.images.isNotEmpty()) product.images else listOf(product.thumbnail)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(scrollState)
    ) {
        // Image Gallery
        val pagerState = rememberPagerState(pageCount = { images.size })
        Box {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) { page ->
                AsyncImage(
                    model = images[page],
                    contentDescription = product.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            // Dots indicator
            if (images.size > 1) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    repeat(images.size) { index ->
                        Box(
                            modifier = Modifier
                                .size(if (pagerState.currentPage == index) 10.dp else 6.dp)
                                .clip(CircleShape)
                                .background(
                                    if (pagerState.currentPage == index) SageGreen
                                    else SageGreen.copy(alpha = 0.4f)
                                )
                        )
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            // Title
            Text(
                text = product.title,
                fontFamily = MadaBoldFont(),
                fontSize = FontSize.EXTRA_MEDIUM,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Rating
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(5) { index ->
                    Text(
                        text = if (index < product.rating.toInt()) "★" else "☆",
                        fontSize = FontSize.REGULAR,
                        color = if (index < product.rating.toInt()) StarFilled else PriceOriginal
                    )
                }
                Text(
                    text = "${product.rating} (${product.reviewCount} reviews)",
                    fontFamily = MadaRegularFont(),
                    fontSize = FontSize.SMALL,
                    color = TextSecondary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Price
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "$%.2f".format(currentPrice),
                    fontFamily = MadaBoldFont(),
                    fontSize = FontSize.EXTRA_MEDIUM,
                    color = SageGreen,
                    fontWeight = FontWeight.Bold
                )
                val origPrice = if (product.hasVariants) {
                    product.variantCombinations.find { it.dimensionValues == selectedVariants }?.originalPrice
                } else {
                    product.originalPrice
                }
                if (origPrice != null && origPrice > currentPrice) {
                    Text(
                        text = "$%.2f".format(origPrice),
                        fontFamily = MadaRegularFont(),
                        fontSize = FontSize.REGULAR,
                        color = PriceOriginal,
                        textDecoration = TextDecoration.LineThrough
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Variants
            if (product.hasVariants && product.variantDimensions.isNotEmpty()) {
                product.variantDimensions.forEach { dimension ->
                    Text(
                        text = "${dimension.name}:",
                        fontFamily = MadaSemiBoldFont(),
                        fontSize = FontSize.REGULAR,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        dimension.values.forEach { value ->
                            val isSelected = selectedVariants[dimension.name] == value
                            FilterChip(
                                selected = isSelected,
                                onClick = { onSelectVariant(dimension.name, value) },
                                label = {
                                    Text(
                                        text = value,
                                        fontFamily = MadaMediumFont(),
                                        fontSize = FontSize.SMALL
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = FilterChipSelected,
                                    containerColor = FilterChipUnselected,
                                    selectedLabelColor = TextPrimary,
                                    labelColor = TextSecondary
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // Description
            Text(
                text = "Description:",
                fontFamily = MadaSemiBoldFont(),
                fontSize = FontSize.REGULAR,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = product.description,
                fontFamily = MadaRegularFont(),
                fontSize = FontSize.REGULAR,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Special Instructions
            if (product.allowsSpecialInstructions) {
                Text(
                    text = "Special instructions:",
                    fontFamily = MadaSemiBoldFont(),
                    fontSize = FontSize.REGULAR,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BorderIdle, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    if (specialInstructions.isEmpty()) {
                        Text(
                            text = "Add any special instructions...",
                            fontFamily = MadaRegularFont(),
                            fontSize = FontSize.REGULAR,
                            color = InputPlaceholder
                        )
                    }
                    BasicTextField(
                        value = specialInstructions,
                        onValueChange = onSpecialInstructionsChanged,
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(
                            fontFamily = MadaRegularFont(),
                            fontSize = FontSize.REGULAR,
                            color = TextPrimary
                        ),
                        minLines = 2
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Quantity selector
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Qty:",
                    fontFamily = MadaSemiBoldFont(),
                    fontSize = FontSize.REGULAR,
                    color = TextPrimary
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .border(1.dp, BorderIdle, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "−",
                        fontSize = FontSize.EXTRA_MEDIUM,
                        color = if (quantity > 1) TextPrimary else TextSecondary,
                        modifier = Modifier.clickable { onDecrementQuantity() }
                    )
                    Text(
                        text = quantity.toString(),
                        fontFamily = MadaBoldFont(),
                        fontSize = FontSize.EXTRA_REGULAR,
                        color = TextPrimary
                    )
                    Text(
                        text = "+",
                        fontSize = FontSize.EXTRA_MEDIUM,
                        color = TextPrimary,
                        modifier = Modifier.clickable { onIncrementQuantity() }
                    )
                }
            }

            if (!isInStock) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Out of stock",
                    fontFamily = MadaMediumFont(),
                    fontSize = FontSize.SMALL,
                    color = CategoryRed
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Add to Cart button
            CustomePrimaryButton(
                text = if (isAddingToCart) "Adding..." else "Add to Cart  ${"$%.2f".format(currentPrice * quantity)}",
                enabled = isInStock && !isAddingToCart,
                onClick = onAddToCart
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}