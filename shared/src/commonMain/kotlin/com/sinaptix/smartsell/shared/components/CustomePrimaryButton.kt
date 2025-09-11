package com.sinaptix.smartsell.shared.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sinaptix.smartsell.shared.resources.Alpha
import com.sinaptix.smartsell.shared.resources.AppStrings
import com.sinaptix.smartsell.shared.resources.ButtonDisabled
import com.sinaptix.smartsell.shared.resources.ButtonPrimary
import com.sinaptix.smartsell.shared.resources.FontSize
import com.sinaptix.smartsell.shared.resources.TextPrimary
import com.sinaptix.smartsell.shared.util.asStringRes
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun CustomePrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: DrawableResource? = null,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = ButtonPrimary,
            contentColor = TextPrimary,
            disabledContainerColor = ButtonDisabled,
            disabledContentColor = TextPrimary.copy(alpha = Alpha.DISABLED)
        ),
        contentPadding = PaddingValues(all = 20.dp)
    ) {
        if(icon != null) {
            Icon(
                modifier = Modifier.size(14.dp),
                painter = painterResource(icon),
                contentDescription = AppStrings.Descriptions.descriptIconButton.asStringRes()
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = FontSize.REGULAR,
            fontWeight = FontWeight.Medium
        )
    }
}