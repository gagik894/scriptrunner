package com.gagik.scriptrunner

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.gagik.scriptrunner.presentation.MainEffect
import com.gagik.scriptrunner.presentation.MainIntent
import com.gagik.scriptrunner.presentation.MainState
import com.gagik.scriptrunner.presentation.MainViewModel
import com.gagik.scriptrunner.ui.components.DraggableDivider
import com.gagik.scriptrunner.ui.editor.EditorPane
import com.gagik.scriptrunner.ui.output.OutputPane
import com.gagik.scriptrunner.ui.theme.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.collections.mutableListOf

@Composable
fun App() {
    val viewModel = remember { MainViewModel() }
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

    var editorWeight by remember { mutableStateOf(0.5f) }
    val density = LocalDensity.current
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val isCompact = maxWidth < 800.dp

        val totalSizePx = with(density) {
            if (isCompact) maxHeight.toPx() else maxWidth.toPx()
        }

        val editorContent = @Composable {
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
        }

        val outputContent = @Composable {
            OutputPane(
                runState = state.runState,
                exitCode = state.exitCode,
                outputLines = state.outputLines,
                onJumpToLine = {
                    //TODO: Implement jump to line in editor
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        val resizeLogic = { deltaPx: Float ->
            val weightDelta = deltaPx / totalSizePx
            editorWeight = (editorWeight + weightDelta).coerceIn(0.2f, 0.8f)
        }

        if (isCompact) {
            Column(Modifier.fillMaxSize()) {
                Box(Modifier.weight(editorWeight)) { editorContent() }

                DraggableDivider(
                    orientation = Orientation.Vertical,
                    onResize = resizeLogic
                )

                Box(Modifier.weight(1f - editorWeight)) { outputContent() }
            }
        } else {
            Row(Modifier.fillMaxSize()) {
                Box(Modifier.weight(editorWeight)) { editorContent() }

                DraggableDivider(
                    orientation = Orientation.Horizontal,
                    onResize = resizeLogic
                )

                Box(Modifier.weight(1f - editorWeight)) { outputContent() }
            }
        }
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
            effects = flowOf(),
            onIntent = {}
        )
    }
}