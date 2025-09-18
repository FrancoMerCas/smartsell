package com.sinaptix.smartsell.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sinaptix.smartsell.shared.components.CustomeTextField
import com.sinaptix.smartsell.shared.components.dialog.CuntryPickerDialog
import com.sinaptix.smartsell.shared.domain.Country
import com.sinaptix.smartsell.shared.resources.AppStrings
import com.sinaptix.smartsell.shared.util.asStringRes

@Composable
fun ProfileForm(
    modifier: Modifier = Modifier,
    firstName: String,
    onFirstNameChange: (String) -> Unit,
    lastName: String,
    onLastNameChange: (String) -> Unit,
    email: String,
    city: String,
    onCityChange: (String) -> Unit,
    zip: Int?,
    onZipChange: (Int?) -> Unit,
    address: String,
    onAddressChange: (String) -> Unit,
    phoneNumber: String?,
    onPhoneNumberChange: (String) -> Unit
) {
    CuntryPickerDialog(
        country = Country.Mexico,
        onDismiss = {},
        onConfirmClick = {}
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = 24.dp,
                vertical = 12.dp
            )
            .verticalScroll(state = rememberScrollState())
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CustomeTextField(
            value = firstName,
            onValueChange = onFirstNameChange,
            placeHolder = AppStrings.PlaceHolder.placeholderFirstName.asStringRes(),
            error = firstName.length !in 3..50
        )
        CustomeTextField(
            value = lastName,
            onValueChange = onLastNameChange,
            placeHolder = AppStrings.PlaceHolder.placeholderLastName.asStringRes(),
            error = lastName.length !in 3..50
        )
        CustomeTextField(
            value = email,
            onValueChange = {},
            placeHolder = AppStrings.PlaceHolder.placeholderEmail.asStringRes(),
            enable = false
        )
        CustomeTextField(
            value = city,
            onValueChange = onCityChange,
            placeHolder = AppStrings.PlaceHolder.placeholderCity.asStringRes(),
            error = city.length !in 3..50
        )
        CustomeTextField(
            value = "${zip ?: ""}",
            onValueChange = { zipData ->
                onZipChange(zipData.toIntOrNull())
            },
            placeHolder = AppStrings.PlaceHolder.placeholderZip.asStringRes(),
            error = zip.toString().length < 5
        )
        CustomeTextField(
            value = address,
            onValueChange = onAddressChange,
            placeHolder = AppStrings.PlaceHolder.placeholderAddress.asStringRes(),
            error = city.length !in 3..50
        )
        CustomeTextField(
            value = phoneNumber ?: "",
            onValueChange = onPhoneNumberChange,
            placeHolder = AppStrings.PlaceHolder.placeholderPhoneNumber.asStringRes(),
            error = phoneNumber.toString().length !in 5..10
        )
    }
}