package com.sinaptix.smartsell.shared.domain

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: String,
    val name: String,
    val slug: String,
    val description: String? = null,
    val color: String = "#819A91",
    val iconUrl: String? = null,
    val sortOrder: Int = 0
)