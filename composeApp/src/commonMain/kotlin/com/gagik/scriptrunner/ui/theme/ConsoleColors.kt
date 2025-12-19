package com.gagik.scriptrunner.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

/**
 * Console-specific colors that derive from the current theme.
 */
@Immutable
data class ConsoleColors(
    val stdout: Color,
    val stderr: Color,
    val system: Color,
    val link: Color,
    val success: Color,
    val running: Color,
    val stopping: Color
)

/**
 * Provides theme-aware console colors.
 * Colors are derived from MaterialTheme and ExtendedTheme.
 */
object ConsoleTheme {
    val colors: ConsoleColors
        @Composable
        @ReadOnlyComposable
        get() = ConsoleColors(
            stdout = MaterialTheme.colorScheme.onSurfaceVariant,
            stderr = MaterialTheme.colorScheme.error,
            system = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            link = ExtendedTheme.colorScheme.link,
            success = ExtendedTheme.colorScheme.success,
            running = ExtendedTheme.colorScheme.warning,
            stopping = MaterialTheme.colorScheme.outline
        )
}
