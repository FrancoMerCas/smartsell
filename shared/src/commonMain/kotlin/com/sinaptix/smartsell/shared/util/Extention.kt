package com.sinaptix.smartsell.shared.util

import androidx.compose.runtime.Composable
import com.sinaptix.smartsell.shared.domain.Country
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun StringResource.asStringRes(): String = stringResource(this)

@Composable
fun StringResource.asStringRes(vararg args: Any): String = stringResource(this, *args)

fun List<Country>.filterByCountry(query: String): List<Country> {
    val queryLower = query.lowercase()
    val queryInt = query.toIntOrNull()

    return this.filter { country ->
        country.name.lowercase().contains(queryLower)
                || country.name.lowercase().contains(queryLower)
                || (queryInt != null && country.dialCode == queryInt)
    }
}