package com.gagik.scriptrunner.presentation

sealed class MainEffect {
    data class ScrollToLine(val lineNumber: Int) : MainEffect()
    data class ShowErrorToast(val message: String) : MainEffect()
}