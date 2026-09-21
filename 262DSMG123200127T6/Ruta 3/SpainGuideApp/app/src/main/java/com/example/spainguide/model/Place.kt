package com.example.spainguide.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

enum class CategoryAccent {
    PRIMARY_CONTAINER,
    SECONDARY_CONTAINER,
    TERTIARY_CONTAINER,
    PRIMARY_SOLID
}

data class Category(
    val id: Int,
    @StringRes val nameRes: Int,
    val icon: ImageVector,
    val accent: CategoryAccent
)

data class Place(
    val id: Int,
    val categoryId: Int,
    @StringRes val nameRes: Int,
    @StringRes val descriptionRes: Int,
    @DrawableRes val imageRes: Int
)