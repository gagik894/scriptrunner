package com.gagik.scriptrunner.ui.output

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Centralized defaults for the Console/Output component.
 */
@Immutable
data class ConsoleDefaults(
    val fontSize: TextUnit = 13.sp,
    val lineHeight: TextUnit = 20.sp,
    val statusFontSize: TextUnit = 12.sp,
    val contentPadding: Dp = 8.dp
)

/**
 * CompositionLocal used to provide [ConsoleDefaults] throughout the output hierarchy.
 */
val LocalConsoleDefaults = staticCompositionLocalOf { ConsoleDefaults() }
