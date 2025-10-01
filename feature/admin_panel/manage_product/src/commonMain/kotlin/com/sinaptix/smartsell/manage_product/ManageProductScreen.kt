package com.sinaptix.smartsell.manage_product

import ContentWithMessageBar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.sinaptix.smartsell.shared.components.CustomeAlertTextField
import com.sinaptix.smartsell.shared.components.CustomePrimaryButton
import com.sinaptix.smartsell.shared.components.CustomeTextField
import com.sinaptix.smartsell.shared.resources.AppIcon
import com.sinaptix.smartsell.shared.resources.AppStrings
import com.sinaptix.smartsell.shared.resources.BorderIdle
import com.sinaptix.smartsell.shared.resources.ButtonPrimary
import com.sinaptix.smartsell.shared.resources.FontSize
import com.sinaptix.smartsell.shared.resources.IconPrimary
import com.sinaptix.smartsell.shared.resources.MadaVariableWghtFont
import com.sinaptix.smartsell.shared.resources.Surface
import com.sinaptix.smartsell.shared.resources.SurfaceGreenLighter
import com.sinaptix.smartsell.shared.resources.SurfaceLighter
import com.sinaptix.smartsell.shared.resources.TextPrimary
import com.sinaptix.smartsell.shared.util.asStringRes
import org.jetbrains.compose.resources.Resource
import org.jetbrains.compose.resources.painterResource
import rememberMessageBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageProductScreen(
    id: String?,
    navigateBack: () -> Unit
) {
    val messageBarState = rememberMessageBarState()

    Scaffold(
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (id == null) AppStrings.Titles.titleManageProductNew.asStringRes()
                        else AppStrings.Titles.titleManageProductNew.asStringRes(),
                        fontFamily = MadaVariableWghtFont(),
                        fontSize = FontSize.LARGE,
                        color = TextPrimary,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = navigateBack
                    ) {
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
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(
                        bottom = 24.dp,
                        top = 12.dp
                    )
                    .imePadding()
            ) {

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(size = 12.dp))
                            .border(
                                width = 1.dp,
                                color = BorderIdle,
                                shape = RoundedCornerShape(size = 12.dp)
                            )
                            .background(SurfaceLighter)
                            .clickable {  },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(AppIcon.Icon.Plus),
                            contentDescription = "",
                            tint = IconPrimary
                        )
                    }
                    CustomeTextField(
                        value = "",
                        onValueChange = {},
                        placeHolder = "Title"
                    )
                    CustomeTextField(
                        modifier = Modifier.height(168.dp),
                        value = "",
                        onValueChange = {},
                        placeHolder = "Description",
                        expanded = true
                    )
                    CustomeAlertTextField(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Protein",
                        onClick = {}
                    )
                    CustomeTextField(
                        value = "",
                        onValueChange = {},
                        placeHolder = "Weight (Optional)",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                    CustomeTextField(
                        value = "",
                        onValueChange = {},
                        placeHolder = "Flavors (Optional)"
                    )
                    CustomeTextField(
                        value = "",
                        onValueChange = {},
                        placeHolder = "Price",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
                CustomePrimaryButton(
                    text = if (id == null) "Add new product"
                    else "Update",
                    icon = if (id == null) AppIcon.Icon.Plus
                    else AppIcon.Icon.Checkmark,
                    onClick = {}
                )
            }
        }
    }
}
