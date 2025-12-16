package com.gagik.scriptrunner.ui.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun EditorPane(
    text: String,
    language: String,
    runState: RunState,
    supportedLanguages: List<String>,
    onTextChange: (String) -> Unit,
    onLanguageChange: (String) -> Unit,
    onRun: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        EditorHeader(
            runState = runState,
            supportedLanguages = supportedLanguages,
            selectedLanguage = language,
            onLanguageSelected = onLanguageChange,
            onRunClick = onRun,
            onStopClick = onStop
        )

        CodeEditor(
            text = text,
            onValueChange = onTextChange,
            modifier = Modifier.weight(1f)
        )
    }
}