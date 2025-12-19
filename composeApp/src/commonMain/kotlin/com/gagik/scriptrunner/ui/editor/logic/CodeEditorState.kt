package com.gagik.scriptrunner.ui.editor.logic

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.delay

@Stable
class CodeEditorState(
    val verticalScrollState: ScrollState,
    val horizontalScrollState: ScrollState
) {
    var highlightedLine by mutableStateOf<Int?>(null)
        private set

    /**
     * Calculates the visible window of lines based on the current scroll position and viewport size.
     * @param lineHeightPx The height of a single line in pixels.
     * @param scrollOffsetPx The current vertical scroll offset in pixels.
     * @param viewportHeightPx The height of the viewport in pixels.
     * @return A pair where the first element is the index of the first visible line,
     *         and the second element is the number of visible lines plus a buffer.
     */
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

    /**
     * Smoothly scrolls to the specified line and briefly highlights it.
     * @param line the line number.
     * @param lineHeightPx The height of a single line in pixels.
     * @param highlightDurationMillis How long to keep the highlight visible.
     */
    suspend fun scrollToLine(
        line: Int,
        lineHeightPx: Float,
        highlightDurationMillis: Long
    ) {
        if (lineHeightPx <= 0) return
        highlightedLine = line
        val offset = ((line - 1) * lineHeightPx).toInt()
        verticalScrollState.animateScrollTo(offset)
        delay(highlightDurationMillis)
        if (highlightedLine == line) {
            highlightedLine = null
        }
    }
}

@Composable
fun rememberCodeEditorState(
    verticalScrollState: ScrollState = rememberScrollState(),
    horizontalScrollState: ScrollState = rememberScrollState()
): CodeEditorState {
    return rememberSaveable(verticalScrollState, horizontalScrollState) {
        CodeEditorState(verticalScrollState, horizontalScrollState)
    }
}