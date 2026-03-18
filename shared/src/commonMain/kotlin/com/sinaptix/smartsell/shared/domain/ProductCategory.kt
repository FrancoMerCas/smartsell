package com.sinaptix.smartsell.shared.domain

import androidx.compose.ui.graphics.Color
import com.sinaptix.smartsell.shared.resources.CategoryBlue
import com.sinaptix.smartsell.shared.resources.CategoryGreen
import com.sinaptix.smartsell.shared.resources.CategoryPurple
import com.sinaptix.smartsell.shared.resources.CategoryRed
import com.sinaptix.smartsell.shared.resources.CategoryYellow

enum class ProductCategory(
    val title: String,
    val color: Color
) {
    Protein(
        title = "Protein",
        color = CategoryYellow
    ),
    Creatine(
        title = "Creatine",
        color = CategoryBlue
    ),
    PreWorkout(
        title = "Pre-Workout",
        color = CategoryGreen
    ),
    Gainers(
        title = "Gainers",
        color = CategoryPurple
    ),
    Accessories(
        title = "Accessories",
        color = CategoryRed
    )
}