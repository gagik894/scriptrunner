package com.gagik.scriptrunner.ui.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gagik.scriptrunner.domain.models.RunState
import com.gagik.scriptrunner.domain.models.ScriptLanguage
import com.gagik.scriptrunner.ui.editor.components.CodeEditor
import com.gagik.scriptrunner.ui.editor.components.EditorHeader
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun EditorPane(
    text: String,
    selectedLanguage: ScriptLanguage,
    runState: RunState,
    onTextChange: (String) -> Unit,
    onLanguageChange: (ScriptLanguage) -> Unit,
    onRun: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier,
    events: Flow<EditorEvent> = emptyFlow()
) {
    Column(modifier = modifier.fillMaxSize()) {
        EditorHeader(
            runState = runState,
            selectedLanguage = selectedLanguage,
            onLanguageSelected = onLanguageChange,
            onRunClick = onRun,
            onStopClick = onStop
        )

        CodeEditor(
            text = text,
            onValueChange = onTextChange,
            language = selectedLanguage,
            modifier = Modifier.weight(1f),
            events = events
        )
    }
}