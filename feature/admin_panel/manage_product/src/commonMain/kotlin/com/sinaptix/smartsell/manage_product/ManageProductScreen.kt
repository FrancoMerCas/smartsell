package com.sinaptix.smartsell.manage_product

import ContentWithMessageBar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sinaptix.smartsell.shared.components.CustomeLoadingCard
import com.sinaptix.smartsell.shared.components.CustomePrimaryButton
import com.sinaptix.smartsell.shared.components.CustomeTextField
import com.sinaptix.smartsell.shared.resources.AppIcon
import com.sinaptix.smartsell.shared.resources.AppStrings
import com.sinaptix.smartsell.shared.resources.BorderIdle
import com.sinaptix.smartsell.shared.resources.CheckboxChecked
import com.sinaptix.smartsell.shared.resources.FontSize
import com.sinaptix.smartsell.shared.resources.IconPrimary
import com.sinaptix.smartsell.shared.resources.MadaMediumFont
import com.sinaptix.smartsell.shared.resources.MadaRegularFont
import com.sinaptix.smartsell.shared.resources.MadaSemiBoldFont
import com.sinaptix.smartsell.shared.resources.MadaVariableWghtFont
import com.sinaptix.smartsell.shared.resources.SageGreen
import com.sinaptix.smartsell.shared.resources.Surface
import com.sinaptix.smartsell.shared.resources.SurfaceGreenLighter
import com.sinaptix.smartsell.shared.resources.SurfaceLighter
import com.sinaptix.smartsell.shared.resources.TextPrimary
import com.sinaptix.smartsell.shared.resources.TextSecondary
import com.sinaptix.smartsell.shared.util.RequestState
import com.sinaptix.smartsell.shared.util.asStringRes
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ManageProductScreen(
    id: String?,
    navigateBack: () -> Unit,
    viewModel: ManageProductViewModel = koinViewModel()
) {
    val messageBarState = rememberMessageBarState()

    LaunchedEffect(id) {
        if (id != null) {
            viewModel.initForEdit(id)
        }
    }

    // Category picker dialog
    if (viewModel.showCategoryPicker) {
        val categories = viewModel.categoriesState
        AlertDialog(
            onDismissRequest = { viewModel.toggleCategoryPicker() },
            title = {
                Text(
                    text = "Select Category",
                    fontFamily = MadaSemiBoldFont(),
                    fontSize = FontSize.MEDIUM,
                    color = TextPrimary
                )
            },
            text = {
                if (categories is RequestState.Loading) {
                    CustomeLoadingCard(modifier = Modifier.fillMaxWidth().height(80.dp))
                } else if (categories is RequestState.Success) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        categories.data.forEach { category ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.selectCategory(category) }
                                    .padding(vertical = 8.dp, horizontal = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val categoryColor = parseHexColor(category.color) ?: SageGreen
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(categoryColor, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = category.name,
                                    fontFamily = MadaRegularFont(),
                                    fontSize = FontSize.REGULAR,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                } else {
                    Text(
                        text = "Failed to load categories",
                        fontFamily = MadaRegularFont(),
                        fontSize = FontSize.REGULAR,
                        color = TextSecondary
                    )
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { viewModel.toggleCategoryPicker() }) {
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
                        text = if (id == null)
                            AppStrings.Titles.titleManageProductNew.asStringRes()
                        else
                            AppStrings.Titles.titleManageProductEdit.asStringRes(),
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
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 24.dp, top = 12.dp)
                        .imePadding()
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        // Thumbnail preview
                        if (viewModel.thumbnailUrl.isNotBlank()) {
                            AsyncImage(
                                model = viewModel.thumbnailUrl,
                                contentDescription = "Product thumbnail",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(1.dp, BorderIdle, RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(1.dp, BorderIdle, RoundedCornerShape(12.dp))
                                    .background(SurfaceLighter),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        modifier = Modifier.size(24.dp),
                                        painter = painterResource(AppIcon.Icon.Plus),
                                        contentDescription = "",
                                        tint = IconPrimary
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Add Image URL below",
                                        fontFamily = MadaRegularFont(),
                                        fontSize = FontSize.SMALL,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }

                        // Image URL input
                        SectionLabel("Image URL")
                        CustomeTextField(
                            value = viewModel.thumbnailUrl,
                            onValueChange = { viewModel.thumbnailUrl = it },
                            placeHolder = "https://example.com/image.jpg"
                        )

                        // Title
                        SectionLabel("Title *")
                        CustomeTextField(
                            value = viewModel.title,
                            onValueChange = { viewModel.title = it },
                            placeHolder = "Product title"
                        )

                        // Description
                        SectionLabel("Description *")
                        CustomeTextField(
                            modifier = Modifier.height(120.dp),
                            value = viewModel.description,
                            onValueChange = { viewModel.description = it },
                            placeHolder = "Product description",
                            expanded = true
                        )

                        // Category
                        SectionLabel("Category *")
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SurfaceLighter, RoundedCornerShape(6.dp))
                                .border(1.dp, BorderIdle, RoundedCornerShape(6.dp))
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { viewModel.toggleCategoryPicker() }
                                .padding(horizontal = 16.dp, vertical = 16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = viewModel.selectedCategoryName.ifBlank { "Select Category" },
                                    fontFamily = MadaRegularFont(),
                                    fontSize = FontSize.REGULAR,
                                    color = if (viewModel.selectedCategoryName.isBlank()) TextSecondary else TextPrimary
                                )
                                Icon(
                                    modifier = Modifier.size(16.dp),
                                    painter = painterResource(AppIcon.Icon.RightArrow),
                                    contentDescription = "Select",
                                    tint = TextSecondary
                                )
                            }
                        }

                        // Product Type
                        SectionLabel("Product Type")
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            viewModel.productTypes.forEach { type ->
                                FilterChip(
                                    selected = viewModel.productType == type,
                                    onClick = { viewModel.productType = type },
                                    label = {
                                        Text(
                                            text = type.replaceFirstChar { it.uppercase() },
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

                        // Price and Original Price
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                SectionLabel("Price *")
                                CustomeTextField(
                                    value = viewModel.price,
                                    onValueChange = { viewModel.price = it },
                                    placeHolder = "0.00",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                SectionLabel("Original Price")
                                CustomeTextField(
                                    value = viewModel.originalPrice,
                                    onValueChange = { viewModel.originalPrice = it },
                                    placeHolder = "0.00",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                                )
                            }
                        }

                        // Stock and Weight
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                SectionLabel(if (viewModel.productType == "digital") "Stock (optional)" else "Stock *")
                                CustomeTextField(
                                    value = viewModel.stock,
                                    onValueChange = { viewModel.stock = it },
                                    placeHolder = "0",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                SectionLabel("Weight (g)")
                                CustomeTextField(
                                    value = viewModel.weight,
                                    onValueChange = { viewModel.weight = it },
                                    placeHolder = "Optional",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                                )
                            }
                        }

                        // Prep time (food/service only)
                        if (viewModel.productType == "food" || viewModel.productType == "service") {
                            SectionLabel("Prep time (min)")
                            CustomeTextField(
                                value = viewModel.preparationTime,
                                onValueChange = { viewModel.preparationTime = it },
                                placeHolder = "e.g. 30",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                        }

                        // Flags section
                        SectionLabel("Flags")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CheckboxItem(
                                checked = viewModel.isPopular,
                                onCheckedChange = { viewModel.isPopular = it },
                                label = "Popular"
                            )
                            CheckboxItem(
                                checked = viewModel.isNew,
                                onCheckedChange = { viewModel.isNew = it },
                                label = "New"
                            )
                            CheckboxItem(
                                checked = viewModel.isDiscounted,
                                onCheckedChange = { viewModel.isDiscounted = it },
                                label = "Discounted"
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CheckboxItem(
                                checked = viewModel.allowsSpecialInstructions,
                                onCheckedChange = { viewModel.allowsSpecialInstructions = it },
                                label = "Allows special instructions"
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    CustomePrimaryButton(
                        text = if (id == null) "Add Product" else "Update Product",
                        icon = if (id == null) AppIcon.Icon.Plus else AppIcon.Icon.Checkmark,
                        enabled = viewModel.isFormValid && viewModel.submitState !is RequestState.Loading,
                        onClick = {
                            viewModel.saveProduct(
                                onSuccess = { navigateBack() },
                                onError = { error -> messageBarState.addError(error) }
                            )
                        }
                    )
                }

                // Loading overlay
                if (viewModel.submitState is RequestState.Loading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CustomeLoadingCard()
                    }
                }
            }
        }
    }
}

private fun parseHexColor(hex: String): Color? {
    return try {
        val cleaned = hex.trimStart('#')
        val long = cleaned.toLong(16)
        if (cleaned.length == 6) {
            Color(long or 0xFF000000)
        } else if (cleaned.length == 8) {
            Color(long)
        } else null
    } catch (e: Exception) {
        null
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        fontFamily = MadaMediumFont(),
        fontSize = FontSize.SMALL,
        color = TextSecondary
    )
}

@Composable
private fun CheckboxItem(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onCheckedChange(!checked) }
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = CheckboxChecked,
                uncheckedColor = TextSecondary
            )
        )
        Text(
            text = label,
            fontFamily = MadaRegularFont(),
            fontSize = FontSize.SMALL,
            color = TextPrimary
        )
    }
}
