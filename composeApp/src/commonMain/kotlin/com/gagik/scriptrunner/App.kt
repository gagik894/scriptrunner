package com.gagik.scriptrunner

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.gagik.scriptrunner.data.ScriptExecutorImpl
import com.gagik.scriptrunner.domain.usecase.RunScriptUseCase
import com.gagik.scriptrunner.presentation.MainEffect
import com.gagik.scriptrunner.presentation.MainIntent
import com.gagik.scriptrunner.presentation.MainState
import com.gagik.scriptrunner.presentation.MainViewModel
import com.gagik.scriptrunner.ui.components.ResponsiveSplitLayout
import com.gagik.scriptrunner.ui.editor.EditorPane
import com.gagik.scriptrunner.ui.output.OutputPane
import com.gagik.scriptrunner.ui.theme.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun App() {
    val scriptRunner = remember { ScriptExecutorImpl() }
    val runScriptUseCase = remember { RunScriptUseCase(scriptRunner) }
    val viewModel = remember { MainViewModel(runScriptUseCase) }
    val state by viewModel.state.collectAsState()
    AppTheme(darkTheme = true) {
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
    LaunchedEffect(Unit) {
        effects.collect { effect ->
            when (effect) {
                is MainEffect.ScrollToLine -> {
                    //TODO: Implement scroll to line in editor
                }
                is MainEffect.ShowErrorToast -> {
                    //TODO: Implement error toast
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
                modifier = Modifier.fillMaxSize()
            )
        },
        secondaryContent = {
            OutputPane(
                runState = state.runState,
                exitCode = state.exitCode,
                outputLines = emptyList(),
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