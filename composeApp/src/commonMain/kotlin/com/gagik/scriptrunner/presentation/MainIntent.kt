package com.gagik.scriptrunner.presentation

import com.gagik.scriptrunner.domain.models.ScriptLanguage

sealed class MainIntent {
    data class UpdateCode(val code: String) : MainIntent()
    data class ChangeLanguage(val language: ScriptLanguage) : MainIntent()
    data object RunScript : MainIntent()
    data object StopScript : MainIntent()
    data class JumpToLine(val line: Int) : MainIntent()
}