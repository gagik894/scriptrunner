package com.gagik.scriptrunner.ui.modifiers

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.ui.Modifier

actual fun Modifier.resizeCursor(orientation: Orientation): Modifier {
    // No-op on Android
    return this
}