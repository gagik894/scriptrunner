package com.gagik.scriptrunner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.gagik.scriptrunner.domain.models.RunState
import com.gagik.scriptrunner.domain.models.ScriptLanguage
import com.gagik.scriptrunner.ui.editor.EditorPane
import com.gagik.scriptrunner.ui.editor.logic.rememberCodeEditorState
import com.gagik.scriptrunner.ui.output.OutputPane
import com.gagik.scriptrunner.ui.theme.AppTheme
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun App() {
    AppTheme(darkTheme = true) {
        var text by remember { mutableStateOf("") }
        MainScreen(
            state = MainState(
                code = text,
                outputLines = listOf("Compiling...", "Hello Preview")
            ),
            onIntent = {
                when (it) {
                    is AppIntent.UpdateCode -> {
                        text = it.code
                    }
                    else -> {}
                }
            }
        )
    }
}

sealed class AppIntent {
    data class UpdateCode(val code: String) : AppIntent()
    data class UpdateLanguage(val language: ScriptLanguage) : AppIntent()
    data object RunScript : AppIntent()
    data object StopScript : AppIntent()
}

data class MainState(
    // Editor Data
    val code: String = "",
    val language: ScriptLanguage = ScriptLanguage.KOTLIN,

    // Execution Data
    val runState: RunState = RunState.IDLE,
    val exitCode: Int? = null,
    val outputLines: List<String> = emptyList()
)

@Composable
fun MainScreen(
    state: MainState,
    onIntent: (AppIntent) -> Unit
) {
    Row(modifier = Modifier.fillMaxSize()) {

        EditorPane(
            text = state.code,
            selectedLanguage = state.language,
            runState = state.runState,
            onTextChange = { onIntent(AppIntent.UpdateCode(it)) },
            onLanguageChange = { onIntent(AppIntent.UpdateLanguage(it)) },
            onRun = { onIntent(AppIntent.RunScript) },
            onStop = { onIntent(AppIntent.StopScript) },
            modifier = Modifier.weight(1f)
        )

        VerticalDivider(color = Color(0xFF1E1E1E))

        OutputPane(
            runState = state.runState,
            exitCode = state.exitCode,
            outputLines = state.outputLines,
            onJumpToLine = {
                //TODO: Implement jump to line in editor
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    AppTheme {
        MainScreen(
            state = MainState(
                code = "fun main() {\n    println(\"Preview\")\n}",
                outputLines = listOf("Compiling...", "Hello Preview")
            ),
            onIntent = {}
        )
    }
}