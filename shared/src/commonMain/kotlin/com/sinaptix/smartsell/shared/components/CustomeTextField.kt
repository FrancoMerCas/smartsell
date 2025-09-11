package com.sinaptix.smartsell.shared.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.TextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.sinaptix.smartsell.shared.resources.Alpha.DISABLED
import com.sinaptix.smartsell.shared.resources.Alpha.HALF
import com.sinaptix.smartsell.shared.resources.BorderError
import com.sinaptix.smartsell.shared.resources.BorderIdle
import com.sinaptix.smartsell.shared.resources.FontSize
import com.sinaptix.smartsell.shared.resources.IconSecondary
import com.sinaptix.smartsell.shared.resources.SurfaceDarker
import com.sinaptix.smartsell.shared.resources.SurfaceLighter
import com.sinaptix.smartsell.shared.resources.TextPrimary

@Composable
fun CustomeTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeHolder: String? = null,
    enable: Boolean = true,
    error: Boolean = false,
    expanded: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text
    )
) {
    val borderColor by animateColorAsState(
        targetValue = if (error) BorderError else BorderIdle
    )

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(size = 6.dp)
            )
            .clip(RoundedCornerShape(size = 6.dp)),
        enabled = enable,
        value = value,
        onValueChange = onValueChange,
        placeholder = if (placeHolder != null) {
            {
                Text(
                    modifier = Modifier.alpha(HALF),
                    text = placeHolder,
                    fontSize = FontSize.REGULAR
                )
            }
        } else null,
        singleLine = !expanded,
        shape = RoundedCornerShape(size = 6.dp),
        keyboardOptions = keyboardOptions,
        colors = TextFieldDefaults.colors(
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            focusedContainerColor = SurfaceLighter,
            unfocusedContainerColor = SurfaceLighter,
            disabledTextColor = TextPrimary.copy(alpha = DISABLED),
            focusedPlaceholderColor = TextPrimary.copy(alpha = HALF),
            unfocusedPlaceholderColor = TextPrimary.copy(alpha = HALF),
            disabledPlaceholderColor = TextPrimary.copy(alpha = DISABLED),
            disabledContainerColor = SurfaceDarker,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            selectionColors = TextSelectionColors( //Is color for cursor in select text
                handleColor = IconSecondary,
                backgroundColor = Color.Unspecified
            )
        )
    )
}