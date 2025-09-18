package com.sinaptix.smartsell.shared.domain

import com.sinaptix.smartsell.shared.resources.AppIcon
import org.jetbrains.compose.resources.DrawableResource

enum class Country(
    val dialCode: Int,
    val code: String,
    val flag: DrawableResource
) {
    Mexico(
        dialCode = 52,
        code = "MX",
        flag = AppIcon.Flag.MexFlag
    ),
    Usa(
        dialCode = 1,
        code = "US",
        flag = AppIcon.Flag.UsaFlag
    ),
    Serbia(
        dialCode = 381,
        code = "RS",
        flag = AppIcon.Flag.SerbiaFlag
    ),
    India(
        dialCode = 91,
        code = "IN",
        flag = AppIcon.Flag.IndiaFlag
    )
}