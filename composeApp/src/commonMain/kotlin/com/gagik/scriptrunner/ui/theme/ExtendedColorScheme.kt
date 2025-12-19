package com.gagik.scriptrunner.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Extended color scheme for semantic colors not in Material 3.
 * These colors adapt to light/dark themes.
 */
@Immutable
data class ExtendedColorScheme(
    val success: Color,
    val onSuccess: Color,
    val successContainer: Color,
    val onSuccessContainer: Color,
    val warning: Color,
    val onWarning: Color,
    val warningContainer: Color,
    val onWarningContainer: Color,
    val link: Color
)

/**
 * Light theme extended colors.
 */
val LightExtendedColorScheme = ExtendedColorScheme(
    success = Color(0xFF4CAF50),
    onSuccess = Color.White,
    successContainer = Color(0xFFC8E6C9),
    onSuccessContainer = Color(0xFF1B5E20),
    warning = Color(0xFFFFC107),
    onWarning = Color.Black,
    warningContainer = Color(0xFFFFF8E1),
    onWarningContainer = Color(0xFFF57F17),
    link = Color(0xFF1976D2)
)

/**
 * Dark theme extended colors.
 */
val DarkExtendedColorScheme = ExtendedColorScheme(
    success = Color(0xFF6A8759),
    onSuccess = Color.White,
    successContainer = Color(0xFF2E7D32),
    onSuccessContainer = Color(0xFFA5D6A7),
    warning = Color(0xFFFFC66D),
    onWarning = Color.Black,
    warningContainer = Color(0xFF5D4037),
    onWarningContainer = Color(0xFFFFE082),
    link = Color(0xFF569CD6)
)

val LocalExtendedColorScheme = staticCompositionLocalOf { DarkExtendedColorScheme }

/**
 * Access extended colors from anywhere in the composition.
 */
object ExtendedTheme {
    val colorScheme: ExtendedColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalExtendedColorScheme.current
}
