package com.gagik.scriptrunner.ui.editor.logic

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember

@Stable
class CodeEditorState(
    val verticalScrollState: ScrollState,
    val horizontalScrollState: ScrollState
) {
    fun getVisibleWindow(
        lineHeightPx: Float,
        scrollOffsetPx: Int,
        viewportHeightPx: Float
    ): Pair<Int, Int> {
        if (lineHeightPx <= 0 || viewportHeightPx <= 0) return 0 to 0

        val firstVisibleLine = (scrollOffsetPx / lineHeightPx).toInt().coerceAtLeast(0)
        val visibleCount = (viewportHeightPx / lineHeightPx).toInt() + 4

        return firstVisibleLine to visibleCount
    }
}

@Composable
fun rememberCodeEditorState(
    verticalScrollState: ScrollState = rememberScrollState(),
    horizontalScrollState: ScrollState = rememberScrollState()
): CodeEditorState {
    return remember(verticalScrollState, horizontalScrollState) {
        CodeEditorState(verticalScrollState, horizontalScrollState)
    }
}