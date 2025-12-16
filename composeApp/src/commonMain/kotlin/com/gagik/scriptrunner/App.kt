package com.gagik.scriptrunner

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.gagik.scriptrunner.ui.editor.CodeEditor
import com.gagik.scriptrunner.ui.editor.EditorPane
import com.gagik.scriptrunner.ui.editor.RunState
import com.gagik.scriptrunner.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme(darkTheme = true) {
        var text by remember { mutableStateOf("") }
        EditorPane(
            text = text,
            language = "Kotlin",
            runState = RunState.IDLE,
            supportedLanguages = listOf("Kotlin", "Java", "JavaScript"),
            onTextChange = { text = it },
            onLanguageChange = {},
            onRun = {},
            onStop = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}