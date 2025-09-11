package com.sinaptix.smartsell.shared.util

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun StringResource.asStringRes(): String = stringResource(this)

@Composable
fun StringResource.asStringRes(vararg args: Any): String = stringResource(this, *args)