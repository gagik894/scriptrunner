package com.gagik.scriptrunner

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.gagik.scriptrunner.data.ScriptExecutorImpl
import com.gagik.scriptrunner.domain.usecase.RunScriptUseCase
import com.gagik.scriptrunner.presentation.MainEffect
import com.gagik.scriptrunner.presentation.MainIntent
import com.gagik.scriptrunner.presentation.MainState
import com.gagik.scriptrunner.presentation.MainViewModel
import com.gagik.scriptrunner.ui.components.ResponsiveSplitLayout
import com.gagik.scriptrunner.ui.editor.EditorEvent
import com.gagik.scriptrunner.ui.editor.EditorPane
import com.gagik.scriptrunner.ui.output.OutputPane
import com.gagik.scriptrunner.ui.theme.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun App() {
    val scriptRunner = remember { ScriptExecutorImpl() }
    val runScriptUseCase = remember { RunScriptUseCase(scriptRunner) }
    val viewModel = remember { MainViewModel(runScriptUseCase) }
    val state by viewModel.state.collectAsState()
    AppTheme(darkTheme = false) {
        MainScreen(
            state = state,
            effects = viewModel.effects,
            onIntent = viewModel::onIntent
        )
    }
}

@Composable
fun MainScreen(
    state: MainState,
    effects: Flow<MainEffect>,
    onIntent: (MainIntent) -> Unit
) {
    val editorEvents = remember { MutableSharedFlow<EditorEvent>(extraBufferCapacity = 1) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        effects.collect { effect ->
            when (effect) {
                is MainEffect.ScrollToLine -> {
                    editorEvents.emit(EditorEvent.ScrollToLine(effect.lineNumber))
                }
                is MainEffect.ShowErrorToast -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    ResponsiveSplitLayout(
        modifier = Modifier.fillMaxSize(),
        primaryContent = {
            EditorPane(
                text = state.code,
                selectedLanguage = state.language,
                runState = state.runState,
                onTextChange = { onIntent(MainIntent.UpdateCode(it)) },
                onLanguageChange = { onIntent(MainIntent.ChangeLanguage(it)) },
                onRun = { onIntent(MainIntent.RunScript) },
                onStop = { onIntent(MainIntent.StopScript) },
                modifier = Modifier.fillMaxSize(),
                events = editorEvents
            )
        },
        secondaryContent = {
            OutputPane(
                runState = state.runState,
                exitCode = state.exitCode,
                outputLines = state.outputLines,
                onJumpToLine = { onIntent(MainIntent.JumpToLine(it)) },
                modifier = Modifier.fillMaxSize()
            )
        }
    )
}

@Preview
@Composable
fun MainScreenPreview() {
    AppTheme {
        MainScreen(
            state = MainState(
                code = "fun main() {\n    println(\"Preview\")\n}",
            ),
            effects = flowOf(),
            onIntent = {}
        )
    }
}