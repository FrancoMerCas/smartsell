package com.sinaptix.smartsell.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponse(
    val _id: String,
    val name: String,
    val slug: String,
    val description: String? = null,
    val color: String = "#819A91",
    val iconUrl: String? = null,
    val sortOrder: Int = 0
)