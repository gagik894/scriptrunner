package com.gagik.scriptrunner.ui.editor

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Centralized defaults for the Code Editor component.
 */
@Immutable
data class EditorDefaults(
    val fontSize: TextUnit = 14.sp,
    val lineHeightMultiplier: Float = 1.6f,
    val gutterWidth: Dp = 40.dp,
    val highlightDurationMillis: Long = 2000L,
    val animationDurationMillis: Int = 300,
    val highlightAlpha: Float = 0.3f,
    val gutterHighlightAlpha: Float = 0.5f,
    val contentPadding: Dp = 8.dp
)

/**
 * CompositionLocal used to provide [EditorDefaults] throughout the editor hierarchy.
 */
val LocalEditorDefaults = staticCompositionLocalOf { EditorDefaults() }
