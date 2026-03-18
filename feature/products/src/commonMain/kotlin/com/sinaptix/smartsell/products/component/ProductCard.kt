package com.sinaptix.smartsell.products.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sinaptix.smartsell.shared.domain.Product
import com.sinaptix.smartsell.shared.resources.CategoryBlue
import com.sinaptix.smartsell.shared.resources.CategoryPurple
import com.sinaptix.smartsell.shared.resources.CategoryYellow
import com.sinaptix.smartsell.shared.resources.FontSize
import com.sinaptix.smartsell.shared.resources.MadaBoldFont
import com.sinaptix.smartsell.shared.resources.MadaMediumFont
import com.sinaptix.smartsell.shared.resources.MadaRegularFont
import com.sinaptix.smartsell.shared.resources.PriceOriginal
import com.sinaptix.smartsell.shared.resources.SageGreen
import com.sinaptix.smartsell.shared.resources.StarFilled
import com.sinaptix.smartsell.shared.resources.Surface
import com.sinaptix.smartsell.shared.resources.TextPrimary
import com.sinaptix.smartsell.shared.resources.TextSecondary
import com.sinaptix.smartsell.shared.resources.White
import com.sinaptix.smartsell.shared.util.formatPrice

@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box {
                AsyncImage(
                    model = product.thumbnail,
                    contentDescription = product.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                    contentScale = ContentScale.Crop
                )
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (product.isNew) {
                        Badge(text = "NEW", color = CategoryBlue)
                    }
                    if (product.isPopular) {
                        Badge(text = "HOT", color = CategoryPurple)
                    }
                    if (product.isDiscounted) {
                        Badge(text = "SALE", color = CategoryYellow)
                    }
                }
            }
            Column(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
            ) {
                Text(
                    text = product.title,
                    fontFamily = MadaMediumFont(),
                    fontSize = FontSize.REGULAR,
                    color = TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                RatingRow(rating = product.rating, reviewCount = product.reviewCount)
                Spacer(modifier = Modifier.height(4.dp))
                PriceRow(price = product.price, originalPrice = product.originalPrice)
            }
        }
    }
}

@Composable
private fun Badge(text: String, color: androidx.compose.ui.graphics.Color) {
    Box(
        modifier = Modifier
            .background(color = color, shape = RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            fontFamily = MadaBoldFont(),
            fontSize = FontSize.EXTRA_SMALL,
            color = White
        )
    }
}

@Composable
private fun RatingRow(rating: Double, reviewCount: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        val filledStars = rating.toInt()
        repeat(5) { index ->
            Text(
                text = if (index < filledStars) "★" else "☆",
                fontSize = FontSize.SMALL,
                color = if (index < filledStars) StarFilled else PriceOriginal
            )
        }
        Text(
            text = "(${reviewCount})",
            fontFamily = MadaRegularFont(),
            fontSize = FontSize.EXTRA_SMALL,
            color = TextSecondary
        )
    }
}

@Composable
private fun PriceRow(price: Double, originalPrice: Double?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = price.formatPrice(),
            fontFamily = MadaBoldFont(),
            fontSize = FontSize.REGULAR,
            color = SageGreen,
            fontWeight = FontWeight.Bold
        )
        if (originalPrice != null && originalPrice > price) {
            Text(
                text = originalPrice.formatPrice(),
                fontFamily = MadaRegularFont(),
                fontSize = FontSize.SMALL,
                color = PriceOriginal,
                textDecoration = TextDecoration.LineThrough
            )
        }
    }
}