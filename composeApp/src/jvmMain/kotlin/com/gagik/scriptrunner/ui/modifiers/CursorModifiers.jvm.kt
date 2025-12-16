package com.gagik.scriptrunner.ui.modifiers

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import java.awt.Cursor

actual fun Modifier.resizeCursor(orientation: Orientation): Modifier {
    val cursorId = when (orientation) {
        Orientation.Horizontal -> Cursor.W_RESIZE_CURSOR
        Orientation.Vertical -> Cursor.N_RESIZE_CURSOR
    }
    return this.pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(cursorId)))
}