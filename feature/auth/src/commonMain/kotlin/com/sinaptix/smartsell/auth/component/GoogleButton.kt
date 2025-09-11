package com.sinaptix.smartsell.auth.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.sinaptix.smartsell.shared.resources.AppIcon
import com.sinaptix.smartsell.shared.resources.AppImage
import com.sinaptix.smartsell.shared.resources.AppStrings
import com.sinaptix.smartsell.shared.resources.BorderIdle
import com.sinaptix.smartsell.shared.resources.ButtonPrimary
import com.sinaptix.smartsell.shared.resources.FontSize
import com.sinaptix.smartsell.shared.resources.IconSecondary
import com.sinaptix.smartsell.shared.resources.TextCreme
import com.sinaptix.smartsell.shared.util.asStringRes
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun GoogleButton(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    primaryText: String = AppStrings.Auth.authLoggingInGoogle.asStringRes(),
    secondaryText: String = AppStrings.Auth.authWaith.asStringRes(),
    icon: DrawableResource = AppImage.Image.GoogleLogo,
    shape: Shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp),
    backgroundColor: Color = ButtonPrimary,
    borderColor: Color = BorderIdle,
    progressIndicatorColor: Color = IconSecondary,
    onClick: () -> Unit,
) {
    var buttonText by remember { mutableStateOf(primaryText) }

    LaunchedEffect(loading) {
        buttonText = if (loading) secondaryText else primaryText
    }

    Surface(
        modifier = modifier
            .clip(shape)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = shape
            )
            .clickable(enabled = !loading) { onClick() },
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 20.dp)
                .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            AnimatedContent(
                targetState = loading
            ) { loadingState ->
                if (!loadingState) {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = AppStrings.Descriptions.descriptIconGoogleLogo.asStringRes(),
                        tint = Color.Unspecified
                    )
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = progressIndicatorColor
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = buttonText,
                color = TextCreme,
                fontSize = FontSize.REGULAR
            )
        }
    }
}