package com.kracubo.app.ui.customscrollbar

import androidx.compose.ui.geometry.Rect

data class ScrollbarMeasurements(
    val barBounds: Rect,
    val indicatorBounds: Rect,
    val alpha: Float,
)