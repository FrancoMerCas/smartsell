package com.sinaptix.smartsell.profile

import ContentWithMessageBar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sinaptix.smartsell.profile.component.ProfileForm
import com.sinaptix.smartsell.shared.resources.Surface
import androidx.compose.ui.unit.dp
import com.sinaptix.smartsell.shared.components.CustomeInfoCard
import com.sinaptix.smartsell.shared.components.CustomeLoadingCard
import com.sinaptix.smartsell.shared.components.CustomePrimaryButton
import com.sinaptix.smartsell.shared.resources.AppIcon
import com.sinaptix.smartsell.shared.resources.AppImage
import com.sinaptix.smartsell.shared.resources.AppStrings
import com.sinaptix.smartsell.shared.resources.FontSize
import com.sinaptix.smartsell.shared.resources.IconPrimary
import com.sinaptix.smartsell.shared.resources.MadaVariableWghtFont
import com.sinaptix.smartsell.shared.resources.SurfaceGreenLighter
import com.sinaptix.smartsell.shared.resources.TextPrimary
import com.sinaptix.smartsell.shared.util.DisplayResult
import com.sinaptix.smartsell.shared.util.asStringRes
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navigateBack: () -> Unit
) {
    val viewModel = koinViewModel<ProfileViewModel>()
    val screenState = viewModel.screenState
    val uiState = viewModel.uiState
    val messageBarState = rememberMessageBarState()
    val isFormValid = viewModel.isFormValid

    val messageSuccesUpdate = AppStrings.Advices.adviceUpdateSuccess.asStringRes()

    Scaffold(
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = AppStrings.Titles.titleMyProfile.asStringRes(),
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
            contentBackgroundColor = Surface,
            modifier = Modifier
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                ),
            messageBarState = messageBarState,
            errorMaxLines = 2
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)
                    .padding(
                        top = 24.dp,
                        bottom = 24.dp
                    )
                    .imePadding()
            ) {
                screenState.DisplayResult(
                    onLoading = {
                        CustomeLoadingCard(modifier = Modifier.fillMaxSize())
                    },
                    onSuccess = { state ->
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            ProfileForm(
                                modifier = Modifier.weight(1f),
                                country = uiState.country,
                                onCountrySelect = viewModel::setCountry,
                                firstName = uiState.firstName,
                                onFirstNameChange = viewModel::setFirstName,
                                lastName = uiState.lastName,
                                onLastNameChange = viewModel::setLastName,
                                email = uiState.email,
                                city = uiState.city ?: "",
                                onCityChange = viewModel::setCity,
                                zip = uiState.zip,
                                onZipChange = viewModel::setZip,
                                address = uiState.address ?: "",
                                onAddressChange = viewModel::setAddress,
                                phoneNumber = uiState.phoneNumber?.number,
                                onPhoneNumberChange = viewModel::setPhoneNumber
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            CustomePrimaryButton(
                                text = AppStrings.Buttons.buttonUpdate.asStringRes(),
                                icon = AppIcon.Icon.Checkmark,
                                enabled = isFormValid,
                                onClick = {
                                    viewModel.updateCustomer(
                                        onSuccess = {
                                            messageBarState.addSuccess(messageSuccesUpdate)
                                        },
                                        onError = { errorMessage ->
                                            messageBarState.addError(message = errorMessage)
                                        }
                                    )
                                }
                            )
                        }
                    },
                    onError = { error ->
                        CustomeInfoCard(
                            image = AppImage.Image.Cat,
                            title = AppStrings.Errors.errorGeneric.asStringRes(),
                            subTitle = error
                        )
                    }
                )
            }
        }
    }
}