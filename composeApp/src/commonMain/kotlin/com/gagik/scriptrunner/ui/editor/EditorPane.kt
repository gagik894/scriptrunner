package com.gagik.scriptrunner.ui.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gagik.scriptrunner.domain.models.RunState
import com.gagik.scriptrunner.domain.models.ScriptLanguage
import com.gagik.scriptrunner.ui.editor.components.CodeEditor
import com.gagik.scriptrunner.ui.editor.components.EditorHeader

@Composable
fun EditorPane(
    text: String,
    selectedLanguage: ScriptLanguage,
    runState: RunState,
    onTextChange: (String) -> Unit,
    onLanguageChange: (ScriptLanguage) -> Unit,
    onRun: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
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
            language = ScriptLanguage.KOTLIN,
            modifier = Modifier.weight(1f)
        )
    }
}