package com.kracubo.app.ui.customscrollbar

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.max
import kotlin.math.min

class ScrollbarLayout(
    val layoutDirection: LayoutDirection,
    val orientation: Orientation,
    val viewPortLength: Float,
    val viewPortCrossAxisLength: Float,
    val contentLength: Float,
    val contentOffset: Int,
    val scrollbarAlpha: Float,
    override val density: Float,
    override val fontScale: Float,
) : Density {
    private val isVertical get() = orientation == Orientation.Vertical

    /**
     * Calculates the bar (track) length with the given padding
     */
    fun calculateBarLength(
        topPadding: Float = 0f,
        startPadding: Float = 0f,
        bottomPadding: Float = 0f,
        endPadding: Float = 0f,
    ): Float {
        val barPadding =
            if (isVertical) {
                topPadding + bottomPadding
            } else {
                startPadding + endPadding
            }
        return viewPortLength - barPadding
    }

    /**
     * Calculates indicator length required for the given [scrollbarLength] and contentLength
     * within the given [minimumIndicatorLength] and [maximumIndicatorLength] values
     */
    fun calculateIndicatorLength(
        scrollbarLength: Float,
        minimumIndicatorLength: Float = 24f,
        maximumIndicatorLength: Float = Float.MAX_VALUE,
    ): Float =
        min(
            max(calculateStandardIndicatorLength(scrollbarLength), minimumIndicatorLength),
            maximumIndicatorLength,
        )

    private fun calculateStandardIndicatorLength(scrollbarLength: Float): Float =
        min((scrollbarLength / contentLength) * viewPortLength, scrollbarLength)

    /**
     * Calculates the offset position of the scrollbar indicator (thumb) within the scrollbar track.
     * This offset helps to position the indicator correctly based on the scrollbar's and indicator's lengths.
     *
     * @param scrollbarLength The total length of the scrollbar's track.
     * @param indicatorLength The length of the scrollbar's indicator (thumb).
     * @return The calculated offset of the indicator within the scrollbar.
     */
    fun calculateIndicatorOffset(
        scrollbarLength: Float,
        indicatorLength: Float,
    ): Float {
        if (contentLength <= viewPortLength) return 0f
        return contentOffset * (scrollbarLength - indicatorLength) / (contentLength - viewPortLength)
    }
}
