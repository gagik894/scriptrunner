package com.gagik.scriptrunner.ui.editor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gagik.scriptrunner.domain.models.ScriptLanguage
import com.gagik.scriptrunner.ui.components.AppVerticalScrollbar
import com.gagik.scriptrunner.ui.editor.logic.CodeSyntaxHighlighter
import com.gagik.scriptrunner.ui.editor.logic.rememberCodeEditorState
import com.gagik.scriptrunner.ui.theme.AppTheme
import com.gagik.scriptrunner.ui.theme.getJetBrainsMonoFontFamily
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun CodeEditor(
    text: String,
    onValueChange: (String) -> Unit,
    language: ScriptLanguage,
    modifier: Modifier = Modifier
) {
    val state = rememberCodeEditorState()
    val density = LocalDensity.current

    val fontSize = 14.sp
    val lineHeight = fontSize * 1.6f

    val lineHeightPx = with(density) { lineHeight.toPx() }
    val lineHeightDp = with(density) { lineHeight.toDp() }
    val jetBrainsMono = getJetBrainsMonoFontFamily()

    val commonTextStyle = TextStyle(
        fontFamily = jetBrainsMono,
        fontSize = fontSize,
        fontWeight = FontWeight.Medium,
        lineHeight = lineHeight,
        color = MaterialTheme.colorScheme.onSurface
    )

    val syntaxHighlighter = remember(language) {
        CodeSyntaxHighlighter(language)
    }

    Surface(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {

        BoxWithConstraints(Modifier.fillMaxSize()) {
            val lineCount = text.count { it == '\n' } + 1
            val viewportHeight = constraints.maxHeight.toFloat()

            val windowInfo by remember(state.verticalScrollState.value, viewportHeight, lineHeightPx, lineCount) {
                derivedStateOf {
                    state.getVisibleWindow(
                        lineHeightPx = lineHeightPx,
                        scrollOffsetPx = state.verticalScrollState.value,
                        viewportHeightPx = viewportHeight
                    )
                }
            }

            Row(Modifier.fillMaxSize()) {
                LineNumberGutter(
                    lineCount = lineCount,
                    firstVisibleLineIndex = windowInfo.first,
                    visibleLineCount = windowInfo.second,
                    lineHeight = lineHeightDp,
                    textStyle = commonTextStyle,
                    onLineNumberClick = { },
                    modifier = Modifier.verticalScroll(state.verticalScrollState)
                )

                VerticalDivider(Modifier.fillMaxHeight())

                BasicTextField(
                    value = text,
                    onValueChange = onValueChange,
                    textStyle = commonTextStyle,
                    visualTransformation = syntaxHighlighter,
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(state.verticalScrollState)
                        .horizontalScroll(state.horizontalScrollState)
                        .padding(start = 8.dp),
                    decorationBox = { inner -> inner() }
                )
            }
            AppVerticalScrollbar(
                scrollState = state.verticalScrollState,
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight()
            )
        }
    }
}


private val sampleText = $$"""
    // Sample Kotlin snippet
    package sample

    fun main() {
        println("Hello, CodeEditor Preview!")
        for (i in 1..5) {
            println("Line \$i")
        }
        println("Hello, CodeEditor Preview!")
        for (i in 1..5) {
            println("Line \$i")
        }
        println("Hello, CodeEditor Preview!")
        for (i in 1..5) {
            println("Line \$i")
        }println("Hello, CodeEditor Preview!")
        for (i in 1..5) {
            println("Line \$i")
        }println("Hello, CodeEditor Preview!")
        for (i in 1..5) {
            println("Line \$i")
        }
        println("Hello, CodeEditor Preview!")
        for (i in 1..5) {
            println("Line \$i")
        }  
    }
""".trimIndent()

@Preview(showBackground = true, backgroundColor = 0xFF1E1E1E)
@Composable
fun CodeEditorPreview() {
    var text by remember { mutableStateOf(sampleText) }
    AppTheme {
        CodeEditor(
            text = text,
            onValueChange = { text = it },
            language = ScriptLanguage.KOTLIN,
            modifier = Modifier.fillMaxSize()
        )
    }
}