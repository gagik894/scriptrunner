package com.gagik.scriptrunner.ui.editor


sealed interface EditorEvent {
    /**
     * Scroll to a specific line number.
     * @param line 1-indexed line number.
     */
    data class ScrollToLine(val line: Int) : EditorEvent
}
