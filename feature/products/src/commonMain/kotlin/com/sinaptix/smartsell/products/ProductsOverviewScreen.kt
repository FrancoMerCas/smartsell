package com.sinaptix.smartsell.products

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sinaptix.smartsell.products.component.ProductCard
import com.sinaptix.smartsell.shared.components.CustomeErrorCard
import com.sinaptix.smartsell.shared.components.CustomeLoadingCard
import com.sinaptix.smartsell.shared.domain.Category
import com.sinaptix.smartsell.shared.domain.Product
import com.sinaptix.smartsell.shared.resources.BorderIdle
import com.sinaptix.smartsell.shared.resources.FilterChipSelected
import com.sinaptix.smartsell.shared.resources.FilterChipUnselected
import com.sinaptix.smartsell.shared.resources.FontSize
import com.sinaptix.smartsell.shared.resources.InputPlaceholder
import com.sinaptix.smartsell.shared.resources.MadaBoldFont
import com.sinaptix.smartsell.shared.resources.MadaMediumFont
import com.sinaptix.smartsell.shared.resources.MadaRegularFont
import com.sinaptix.smartsell.shared.resources.SageGreen
import com.sinaptix.smartsell.shared.resources.Surface
import com.sinaptix.smartsell.shared.resources.SurfaceLighter
import com.sinaptix.smartsell.shared.resources.TextPrimary
import com.sinaptix.smartsell.shared.resources.TextSecondary
import com.sinaptix.smartsell.shared.util.RequestState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProductsOverviewScreen(
    initialCategoryId: String? = null,
    onNavigateToProductDetail: (String) -> Unit
) {
    val viewModel = koinViewModel<ProductsOverviewViewModel>()

    LaunchedEffect(initialCategoryId) {
        if (initialCategoryId != null) {
            viewModel.onCategorySelected(initialCategoryId)
        }
    }

    ProductsOverviewContent(
        productsState = viewModel.productsState,
        categoriesState = viewModel.categoriesState,
        selectedCategoryId = viewModel.selectedCategoryId,
        searchQuery = viewModel.searchQuery,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onCategorySelected = viewModel::onCategorySelected,
        onNavigateToProductDetail = onNavigateToProductDetail,
        onRetry = viewModel::refresh
    )
}

@Composable
private fun ProductsOverviewContent(
    productsState: RequestState<List<Product>>,
    categoriesState: RequestState<List<Category>>,
    selectedCategoryId: String?,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onCategorySelected: (String?) -> Unit,
    onNavigateToProductDetail: (String) -> Unit,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceLighter)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Surface)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            SearchBar(
                query = searchQuery,
                onQueryChanged = onSearchQueryChanged
            )
            Spacer(modifier = Modifier.height(12.dp))
            CategoriesRow(
                categoriesState = categoriesState,
                selectedCategoryId = selectedCategoryId,
                onCategorySelected = onCategorySelected
            )
        }

        when (productsState) {
            is RequestState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CustomeLoadingCard()
                }
            }
            is RequestState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CustomeErrorCard(
                        modifier = Modifier.padding(16.dp),
                        message = productsState.message
                    )
                }
            }
            is RequestState.Success -> {
                val products = productsState.data
                val popularProducts = products.filter { it.isPopular }
                val allProducts = products

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (popularProducts.isNotEmpty()) {
                        item(span = { GridItemSpan(2) }) {
                            SectionHeader(title = "Popular")
                        }
                        items(popularProducts) { product ->
                            ProductCard(
                                product = product,
                                onClick = { onNavigateToProductDetail(product.id) }
                            )
                        }
                    }
                    item(span = { GridItemSpan(2) }) {
                        SectionHeader(title = "All Products")
                    }
                    items(allProducts) { product ->
                        ProductCard(
                            product = product,
                            onClick = { onNavigateToProductDetail(product.id) }
                        )
                    }
                }
            }
            else -> {}
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceLighter, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        if (query.isEmpty()) {
            Text(
                text = "Search products...",
                fontFamily = MadaRegularFont(),
                fontSize = FontSize.REGULAR,
                color = InputPlaceholder
            )
        }
        BasicTextField(
            value = query,
            onValueChange = onQueryChanged,
            modifier = Modifier.fillMaxWidth(),
            textStyle = androidx.compose.ui.text.TextStyle(
                fontFamily = MadaRegularFont(),
                fontSize = FontSize.REGULAR,
                color = TextPrimary
            )
        )
    }
}

@Composable
private fun CategoriesRow(
    categoriesState: RequestState<List<Category>>,
    selectedCategoryId: String?,
    onCategorySelected: (String?) -> Unit
) {
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedCategoryId == null,
            onClick = { onCategorySelected(null) },
            label = {
                Text(
                    text = "All",
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
        if (categoriesState is RequestState.Success) {
            categoriesState.data.forEach { category ->
                FilterChip(
                    selected = selectedCategoryId == category.id,
                    onClick = { onCategorySelected(category.id) },
                    label = {
                        Text(
                            text = category.name,
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
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        fontFamily = MadaBoldFont(),
        fontSize = FontSize.EXTRA_REGULAR,
        fontWeight = FontWeight.Bold,
        color = TextPrimary,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}