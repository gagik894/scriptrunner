package com.gagik.scriptrunner.presentation

sealed class MainEffect {
    /**
     * Scroll to a specific line number.
     * @param lineNumber 1-indexed line number.
     */
    data class ScrollToLine(val lineNumber: Int) : MainEffect()

    /**
     * Show an error toast with the provided message.
     * @param message The error message to display.
     */
    data class ShowErrorToast(val message: String) : MainEffect()
}