package com.gagik.scriptrunner.presentation

import com.gagik.scriptrunner.domain.models.RunState
import com.gagik.scriptrunner.domain.models.ScriptLanguage
import com.gagik.scriptrunner.presentation.models.ConsoleUiLine

data class MainState(
    // Editor Data
    val code: String = "",
    val language: ScriptLanguage = ScriptLanguage.KOTLIN,

    // Execution Data
    val runState: RunState = RunState.IDLE,
    val exitCode: Int? = null,
    val outputLines: List<ConsoleUiLine> = emptyList(),
)
