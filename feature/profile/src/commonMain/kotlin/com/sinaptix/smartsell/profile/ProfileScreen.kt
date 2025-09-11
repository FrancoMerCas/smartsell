package com.sinaptix.smartsell.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sinaptix.smartsell.profile.component.ProfileForm
import com.sinaptix.smartsell.shared.resources.Surface

@Composable
fun ProfileScreen(

) {
    Box(
        modifier = Modifier
            .background(Surface)
            .systemBarsPadding()
    ) {
        ProfileForm(
            modifier = Modifier,
            firstName = "",
            onFirstNameChange = {},
            lastName = "",
            onLastNameChange = {},
            email = "",
            city = "",
            onCityChange = {},
            zip = null,
            onZipChange = {},
            address = "",
            onAddressChange = {},
            phoneNumber = null,
            onPhoneNumberChange = {}
        )
    }
}