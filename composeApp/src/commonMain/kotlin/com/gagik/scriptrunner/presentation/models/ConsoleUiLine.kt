package com.gagik.scriptrunner.presentation.models

data class ConsoleUiLine(
    val text: String,
    val type: Type = Type.NORMAL,
    val linkRange: IntRange? = null,
    val targetLineNumber: Int? = null
) {
    enum class Type {
        NORMAL,  // Standard Output
        ERROR,   // Standard Error (Red)
        SYSTEM   // Exit Codes / Status (Gray Italic)
    }
}