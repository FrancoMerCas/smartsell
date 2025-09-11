package com.sinaptix.smartsell.home.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.sinaptix.smartsell.home.domain.BottomBarDestination
import com.sinaptix.smartsell.shared.resources.SurfaceGreenLighter
import com.sinaptix.smartsell.shared.resources.TabIndicator
import com.sinaptix.smartsell.shared.resources.TabUnselected
import org.jetbrains.compose.resources.painterResource

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    selected: BottomBarDestination,
    onSelect: (BottomBarDestination) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(8.dp),
                clip = false
            )
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceGreenLighter)
            .padding(
                vertical = 12.dp,
                horizontal = 24.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        BottomBarDestination.entries.forEach { destination ->
            val animatedTint by animateColorAsState(
                targetValue = if (selected  == destination) TabIndicator else TabUnselected
            )
            Icon(
                modifier = Modifier
                    .clickable {
                        onSelect(destination)
                    },
                painter = painterResource(destination.icon),
                contentDescription = "Bottom bar destination icon",
                tint = animatedTint
            )
        }
    }
}
