package com.sinaptix.smartsell.shared.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sinaptix.smartsell.shared.resources.AppStrings
import com.sinaptix.smartsell.shared.resources.FontSize
import com.sinaptix.smartsell.shared.util.asStringRes
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun CustomeInfoCard(
    modifier: Modifier = Modifier,
    image: DrawableResource,
    title: String,
    subTitle: String
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(60.dp),
            painter = painterResource(image),
            contentDescription = AppStrings.Descriptions.descriptIconError.asStringRes()
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = title,
            fontSize = FontSize.MEDIUM,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = subTitle,
            fontSize = FontSize.REGULAR
        )
    }
}