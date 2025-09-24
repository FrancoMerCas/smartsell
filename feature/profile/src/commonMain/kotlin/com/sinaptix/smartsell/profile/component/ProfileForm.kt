package com.sinaptix.smartsell.profile.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.sinaptix.smartsell.shared.components.CustomeAlertTextField
import com.sinaptix.smartsell.shared.components.CustomeTextField
import com.sinaptix.smartsell.shared.components.dialog.CuntryPickerDialog
import com.sinaptix.smartsell.shared.domain.Country
import com.sinaptix.smartsell.shared.resources.AppStrings
import com.sinaptix.smartsell.shared.util.asStringRes

@Composable
fun ProfileForm(
    modifier: Modifier = Modifier,
    country: Country,
    onCountrySelect: (Country) -> Unit,
    firstName: String,
    onFirstNameChange: (String) -> Unit,
    lastName: String,
    onLastNameChange: (String) -> Unit,
    email: String,
    city: String?,
    onCityChange: (String) -> Unit,
    zip: Int?,
    onZipChange: (Int?) -> Unit,
    address: String?,
    onAddressChange: (String) -> Unit,
    phoneNumber: String?,
    onPhoneNumberChange: (String) -> Unit
) {
    var showCountryDialog by remember { mutableStateOf(false) }

    AnimatedVisibility(
        showCountryDialog
    ) {
        CuntryPickerDialog(
            country = country,
            onDismiss = { showCountryDialog = false },
            onConfirmClick = { selectedCountry ->
                showCountryDialog = false
                onCountrySelect(selectedCountry)
            }
        )
    }
    Column(
        modifier = modifier
            .fillMaxSize()
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
            value = city ?: "",
            onValueChange = onCityChange,
            placeHolder = AppStrings.PlaceHolder.placeholderCity.asStringRes(),
            error = city?.length !in 3..50
        )
        CustomeTextField(
            value = "${zip ?: ""}",
            onValueChange = { zipData ->
                onZipChange(zipData.toIntOrNull())
            },
            placeHolder = AppStrings.PlaceHolder.placeholderZip.asStringRes(),
            error = zip == null || zip.toString().length < 5,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
        CustomeTextField(
            value = address ?: "",
            onValueChange = onAddressChange,
            placeHolder = AppStrings.PlaceHolder.placeholderAddress.asStringRes(),
            error = address?.length !in 3..50
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomeAlertTextField(
                text = "+${country.dialCode}",
                icon = country.flag,
                onClick = {
                    showCountryDialog = true
                }
            )
            Spacer(modifier = Modifier.width(12.dp))
            CustomeTextField(
                value = phoneNumber ?: "",
                onValueChange = onPhoneNumberChange,
                placeHolder = AppStrings.PlaceHolder.placeholderPhoneNumber.asStringRes(),
                error = phoneNumber.toString().length !in 5..30,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
        }
    }
}