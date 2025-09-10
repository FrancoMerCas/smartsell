package com.sinaptix.smartsell.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.sinaptix.smartsell.home.domain.DrawerItem
import com.sinaptix.smartsell.shared.FontSize.EXTRA_LARGE
import com.sinaptix.smartsell.shared.FontSize.REGULAR
import com.sinaptix.smartsell.shared.MadaBlackFont
import com.sinaptix.smartsell.shared.TextCreme
import com.sinaptix.smartsell.shared.TextPrimary
import com.sinaptix.smartsell.shared.TextSecondary

@Composable
fun CustomDrawer(
    onProfileClick: () -> Unit,
    onContactUsClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onAdminPanelClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.6f)
            .padding(horizontal = 12.dp)
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = TextSecondary)) {
                    append("Smart")
                }
                withStyle(style = SpanStyle(color = TextCreme)) {
                    append("Sell")
                }
            },
            textAlign = TextAlign.Center,
            fontFamily = MadaBlackFont(),
            fontSize = EXTRA_LARGE
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Intelligent Sells",
            textAlign = TextAlign.Center,
            fontSize = REGULAR,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(50.dp))

        DrawerItem.entries.take(5).forEach { itemMenu ->
            DrawerItemCard(
                drawerItem = itemMenu,
                onClick = {
                    when(itemMenu) {
                        DrawerItem.Profile -> onProfileClick()
                        DrawerItem.Contact -> onContactUsClick()
                        DrawerItem.SignOut-> onSignOutClick()
                        else -> {

                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        Spacer(modifier = Modifier.weight(1f))
        DrawerItemCard(
            drawerItem = DrawerItem.Admin,
            onClick = onAdminPanelClick
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}