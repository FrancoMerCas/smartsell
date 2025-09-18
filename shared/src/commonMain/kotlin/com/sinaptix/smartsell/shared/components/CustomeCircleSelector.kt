package com.sinaptix.smartsell.shared.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.sinaptix.smartsell.shared.resources.AppIcon
import com.sinaptix.smartsell.shared.resources.AppStrings
import com.sinaptix.smartsell.shared.resources.IconWhite
import com.sinaptix.smartsell.shared.resources.SurfaceBrand
import com.sinaptix.smartsell.shared.resources.SurfaceSecondary
import com.sinaptix.smartsell.shared.util.asStringRes
import org.jetbrains.compose.resources.painterResource

@Composable
fun CustomeCircleSelector(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false
) {
    val animatedBackGround by animateColorAsState(
        targetValue = if (isSelected) SurfaceBrand else SurfaceSecondary
    )
    Box(
        modifier = modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(animatedBackGround),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = isSelected
        ) {
            Icon(
                modifier = Modifier.size(14.dp),
                painter = painterResource(AppIcon.Icon.Checkmark),
                contentDescription = AppStrings.Descriptions.descriptIconCheckmark.asStringRes(),
                tint = IconWhite
            )
        }
    }
}