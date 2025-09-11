package com.sinaptix.smartsell.shared.resources

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import smartsell.shared.generated.resources.Res
import smartsell.shared.generated.resources.mada_black
import smartsell.shared.generated.resources.mada_bold
import smartsell.shared.generated.resources.mada_extrabold
import smartsell.shared.generated.resources.mada_extralight
import smartsell.shared.generated.resources.mada_medium
import smartsell.shared.generated.resources.mada_regular
import smartsell.shared.generated.resources.mada_semibold
import smartsell.shared.generated.resources.mada_variable_wght

@Composable
fun MadaBlackFont() = FontFamily(
    Font(Res.font.mada_black)
)

@Composable
fun MadaBoldFont() = FontFamily(
    Font(Res.font.mada_bold)
)

@Composable
fun MadaExtraBoldFont() = FontFamily(
    Font(Res.font.mada_extrabold)
)

@Composable
fun MadaExtraLightFont() = FontFamily(
    Font(Res.font.mada_extralight)
)

@Composable
fun MadaMediumFont() = FontFamily(
    Font(Res.font.mada_medium)
)

@Composable
fun MadaRegularFont() = FontFamily(
    Font(Res.font.mada_regular)
)

@Composable
fun MadaSemiBoldFont() = FontFamily(
    Font(Res.font.mada_semibold)
)

@Composable
fun MadaVariableWghtFont() = FontFamily(
    Font(Res.font.mada_variable_wght)
)

object FontSize {
    val EXTRA_SMALL = 10.sp
    val SMALL = 12.sp
    val REGULAR = 14.sp
    val EXTRA_REGULAR = 16.sp
    val MEDIUM = 18.sp
    val EXTRA_MEDIUM = 20.sp
    val LARGE = 30.sp
    val EXTRA_LARGE = 40.sp
}