package com.gagik.scriptrunner.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gagik.scriptrunner.domain.models.RunState
import com.gagik.scriptrunner.domain.models.ScriptLanguage
import com.gagik.scriptrunner.domain.models.ScriptOutput
import com.gagik.scriptrunner.domain.provider.ScriptTemplateProvider
import com.gagik.scriptrunner.domain.usecase.RunScriptUseCase
import com.gagik.scriptrunner.presentation.mappers.OutputMapper
import com.gagik.scriptrunner.presentation.models.ConsoleUiLine
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val runScriptUseCase: RunScriptUseCase,
    private val outputMapper: OutputMapper = OutputMapper()
) : ViewModel() {

    private val _state = MutableStateFlow(
        MainState(
            code = ScriptTemplateProvider.getTemplate(ScriptLanguage.KOTLIN),
            language = ScriptLanguage.KOTLIN
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
        val template = ScriptTemplateProvider.getTemplate(newLanguage)
        _state.update { it.copy(language = newLanguage, code = template) }
    }

    private fun runScript() {
        if (_state.value.runState != RunState.IDLE) return
        val currentState = _state.value
        _state.update {
            it.copy(
                runState = RunState.RUNNING,
                outputLines = emptyList(),
                exitCode = null
            )
        }

        executionJob = viewModelScope.launch {
            runScriptUseCase(currentState.code, currentState.language)
                .collect { output ->
                    handleScriptOutput(output)
                }
        }
    }

    private fun handleScriptOutput(output: ScriptOutput) {
        val uiLine = outputMapper.map(output)

        _state.update {
            it.copy(outputLines = it.outputLines + uiLine)
        }

        when (output) {
            is ScriptOutput.Exit -> {
                _state.update {
                    it.copy(runState = RunState.IDLE, exitCode = output.code)
                }
            }

            is ScriptOutput.Error -> {
                _state.update {
                    it.copy(
                        runState = RunState.IDLE,
                        exitCode = -1
                    )
                }
            }

            else -> {
                // Running... do nothing
            }
        }
    }

    private fun stopScript() {
        if (_state.value.runState == RunState.RUNNING) {
            _state.update { it.copy(runState = RunState.STOPPING) }

            executionJob?.cancel()

            _state.update {
                it.copy(
                    runState = RunState.IDLE,
                    outputLines = it.outputLines + ConsoleUiLine(
                        text = "Script execution stopped.",
                        type = ConsoleUiLine.Type.SYSTEM
                    ),
                    exitCode = 130
                )
            }
        }
    }
}