package com.gagik.scriptrunner.ui.modifiers

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon

actual fun Modifier.resizeCursor(orientation: Orientation): Modifier {

    return this.pointerHoverIcon(PointerIcon.Hand)
}