package com.gagik.scriptrunner.ui.editor.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gagik.scriptrunner.domain.models.ScriptLanguage
import com.gagik.scriptrunner.ui.components.AppVerticalScrollbar
import com.gagik.scriptrunner.ui.editor.EditorEvent
import com.gagik.scriptrunner.ui.editor.logic.CodeSyntaxHighlighter
import com.gagik.scriptrunner.ui.editor.logic.rememberCodeEditorState
import com.gagik.scriptrunner.ui.theme.AppTheme
import com.gagik.scriptrunner.ui.theme.getJetBrainsMonoFontFamily
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun CodeEditor(
    text: String,
    onValueChange: (String) -> Unit,
    language: ScriptLanguage,
    modifier: Modifier = Modifier,
    events: Flow<EditorEvent> = emptyFlow()
) {
    val state = rememberCodeEditorState()
    val density = LocalDensity.current

    val fontSize = 14.sp
    val lineHeight = fontSize * 1.6f

    val lineHeightPx = with(density) { lineHeight.toPx() }
    val lineHeightDp = with(density) { lineHeight.toDp() }

    LaunchedEffect(events, lineHeightPx) {
        events.collect { event ->
            when (event) {
                is EditorEvent.ScrollToLine -> {
                    state.scrollToLine(event.line, lineHeightPx)
                }
            }
        }
    }

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

    val highlightColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
    val animatedHighlightColor by animateColorAsState(
        targetValue = if (state.highlightedLine != null) highlightColor else Color.Transparent,
        animationSpec = tween(durationMillis = 300)
    )

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
                    modifier = Modifier.verticalScroll(state.verticalScrollState),
                    highlightedLine = state.highlightedLine
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
                        .drawBehind {
                            state.highlightedLine?.let { line ->
                                val y = (line - 1) * lineHeightPx
                                drawRect(
                                    color = animatedHighlightColor,
                                    topLeft = Offset(0f, y),
                                    size = Size(size.width, lineHeightPx)
                                )
                            }
                        }
                        .padding(start = 8.dp),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
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