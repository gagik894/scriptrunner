package com.gagik.scriptrunner.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Centralized global spacing, sizing, and layout constants.
 * Component-specific defaults should go in their own XxxDefaults files.
 */
object Dimens {
    // Padding
    val HeaderPaddingHorizontal: Dp = 16.dp
    val HeaderPaddingVertical: Dp = 8.dp
    val ContentPadding: Dp = 8.dp

    // Spacers
    val SpacerSmall: Dp = 4.dp
    val SpacerMedium: Dp = 8.dp
    val SpacerLarge: Dp = 16.dp

    // Elevation
    val HeaderElevation: Dp = 4.dp

    // Button/Icon sizes
    val ButtonHeight: Dp = 36.dp
    val IconSizeSmall: Dp = 18.dp

    // Corner radius
    val CornerRadius: Dp = 4.dp

    // Layout breakpoints
    val CompactBreakpoint: Dp = 800.dp

    // Divider
    val DividerThickness: Dp = 1.dp
    val DividerTouchTarget: Dp = 12.dp
}
