package com.gagik.scriptrunner

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.gagik.scriptrunner.domain.models.RunState
import com.gagik.scriptrunner.domain.models.ScriptLanguage
import com.gagik.scriptrunner.ui.editor.EditorPane
import com.gagik.scriptrunner.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme(darkTheme = true) {
        var text by remember { mutableStateOf("") }
        EditorPane(
            text = text,
            selectedLanguage = ScriptLanguage.KOTLIN,
            runState = RunState.IDLE,
            onTextChange = { text = it },
            onLanguageChange = {},
            onRun = {},
            onStop = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}