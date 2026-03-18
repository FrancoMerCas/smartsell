package com.sinaptix.smartsell.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinaptix.smartsell.data.domain.CategoryRepository
import com.sinaptix.smartsell.shared.components.CustomeErrorCard
import com.sinaptix.smartsell.shared.components.CustomeLoadingCard
import com.sinaptix.smartsell.shared.domain.Category
import com.sinaptix.smartsell.shared.resources.FontSize
import com.sinaptix.smartsell.shared.resources.MadaBoldFont
import com.sinaptix.smartsell.shared.resources.MadaMediumFont
import com.sinaptix.smartsell.shared.resources.SageGreen
import com.sinaptix.smartsell.shared.resources.SurfaceLighter
import com.sinaptix.smartsell.shared.resources.TextPrimary
import com.sinaptix.smartsell.shared.resources.White
import com.sinaptix.smartsell.shared.util.RequestState
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

class CategoriesViewModel(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    var categoriesState: RequestState<List<Category>> by mutableStateOf(RequestState.Loading)
        private set

    private val storeId = "default"

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            categoriesState = RequestState.Loading
            categoriesState = categoryRepository.getCategories(storeId)
        }
    }
}

@Composable
fun CategoriesScreen(
    onNavigateToProducts: (categoryId: String) -> Unit
) {
    val viewModel = koinViewModel<CategoriesViewModel>()

    CategoriesContent(
        categoriesState = viewModel.categoriesState,
        onCategoryClick = onNavigateToProducts,
        onRetry = viewModel::loadCategories
    )
}

@Composable
private fun CategoriesContent(
    categoriesState: RequestState<List<Category>>,
    onCategoryClick: (String) -> Unit,
    onRetry: () -> Unit
) {
    when (categoriesState) {
        is RequestState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CustomeLoadingCard()
            }
        }
        is RequestState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CustomeErrorCard(
                    modifier = Modifier.padding(16.dp),
                    message = categoriesState.message
                )
            }
        }
        is RequestState.Success -> {
            val categories = categoriesState.data
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize()
                    .background(SurfaceLighter),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categories) { category ->
                    CategoryCard(
                        category = category,
                        onClick = { onCategoryClick(category.id) }
                    )
                }
            }
        }
        else -> {}
    }
}

@Composable
private fun CategoryCard(
    category: Category,
    onClick: () -> Unit
) {
    val bgColor = parseCategoryColor(category.color)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor.copy(alpha = 0.15f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .background(bgColor, RoundedCornerShape(8.dp))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = category.name.take(1).uppercase(),
                    fontFamily = MadaBoldFont(),
                    fontSize = FontSize.MEDIUM,
                    color = White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = category.name,
                fontFamily = MadaMediumFont(),
                fontSize = FontSize.EXTRA_SMALL,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private fun parseCategoryColor(colorHex: String): Color {
    return try {
        val hex = colorHex.trimStart('#')
        if (hex.length == 6) {
            Color(
                red = hex.substring(0, 2).toInt(16) / 255f,
                green = hex.substring(2, 4).toInt(16) / 255f,
                blue = hex.substring(4, 6).toInt(16) / 255f
            )
        } else SageGreen
    } catch (e: Exception) {
        SageGreen
    }
}
