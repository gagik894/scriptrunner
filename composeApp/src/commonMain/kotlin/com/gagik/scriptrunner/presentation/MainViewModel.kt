package com.gagik.scriptrunner.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gagik.scriptrunner.domain.models.RunState
import com.gagik.scriptrunner.domain.models.ScriptLanguage
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _state = MutableStateFlow(
        MainState(
            code = """
            fun main() {
                println("Hello from Kotlin!")
                Thread.sleep(1000)
                println("Done.")
            }
        """.trimIndent()
        )
    )
    val state = _state.asStateFlow()

    private val _effects = Channel<MainEffect>()
    val effects = _effects.receiveAsFlow()

    private var executionJob: Job? = null

    fun onIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.UpdateCode -> {
                _state.update { it.copy(code = intent.code) }
            }

            is MainIntent.ChangeLanguage -> {
                updateLanguage(intent.language)
            }

            is MainIntent.RunScript -> {
                runScript()
            }

            is MainIntent.StopScript -> {
                stopScript()
            }

            is MainIntent.JumpToLine -> {
                viewModelScope.launch {
                    _effects.send(MainEffect.ScrollToLine(intent.line))
                }
            }
        }
    }


    private fun updateLanguage(newLanguage: ScriptLanguage) {
        val template = when (newLanguage) {
            ScriptLanguage.KOTLIN -> "fun main() {\n    println(\"Hello Kotlin\")\n}"
            ScriptLanguage.SWIFT -> "print(\"Hello Swift\")"
        }
        _state.update { it.copy(language = newLanguage, code = template) }
    }

    private fun runScript() {
        if (_state.value.runState != RunState.IDLE) return

        _state.update {
            it.copy(
                runState = RunState.RUNNING,
                outputLines = emptyList(),
                exitCode = null
            )
        }

        executionJob = viewModelScope.launch {
            //TODO: Implement script execution
        }
    }

    private fun stopScript() {
        if (_state.value.runState == RunState.RUNNING) {
            _state.update { it.copy(runState = RunState.STOPPING) }

            executionJob?.cancel()

            _state.update {
                it.copy(
                    runState = RunState.IDLE,
                    outputLines = it.outputLines + "\n[Terminated by user]",
                    exitCode = 130
                )
            }
        }
    }
}