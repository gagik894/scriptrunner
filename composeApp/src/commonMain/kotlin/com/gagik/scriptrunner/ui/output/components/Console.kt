package com.gagik.scriptrunner.ui.output.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gagik.scriptrunner.ui.theme.AppTheme
import com.gagik.scriptrunner.ui.theme.getJetBrainsMonoFontFamily
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Console(
    outputLines: List<String>,
    onJumpToLine: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LaunchedEffect(outputLines.size) {
        if (outputLines.isNotEmpty()) {
            listState.animateScrollToItem(outputLines.lastIndex)
        }
    }

    SelectionContainer(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize().padding(8.dp)
        ) {
            items(outputLines) { line ->
                ConsoleLine(
                    text = line,
                    onJumpToLine = onJumpToLine
                )
            }
        }
    }
}

@Composable
private fun ConsoleLine(
    text: String,
    onJumpToLine: (Int) -> Unit
) {
    val fontFamily = getJetBrainsMonoFontFamily()

    // Regex matches "Script.kts:5:12:"
    val errorRegex = Regex("""([a-zA-Z0-9_.]+):(\d+):(\d+):""")

    val annotatedString = buildAnnotatedString {
        val match = errorRegex.find(text)

        if (match != null) {
            val lineNumber = match.groupValues[2].toIntOrNull() ?: 0

            withStyle(SpanStyle(color = Color(0xFFBBBBBB), fontFamily = fontFamily)) {
                append(text.substring(0, match.range.first))
            }

            val link = LinkAnnotation.Clickable(
                tag = "error_link",
                linkInteractionListener = {
                    onJumpToLine(lineNumber)
                }
            )

            withLink(link) {
                withStyle(
                    SpanStyle(
                        color = Color(0xFF569CD6),
                        textDecoration = TextDecoration.Underline,
                        fontFamily = fontFamily
                    )
                ) {
                    append(match.value)
                }
            }

            withStyle(SpanStyle(color = Color(0xFFFF6B68), fontFamily = fontFamily)) {
                append(text.substring(match.range.last + 1))
            }
        } else {
            // Normal Line (No error found)
            withStyle(SpanStyle(color = Color(0xFFBBBBBB), fontFamily = fontFamily)) {
                append(text)
            }
        }
    }

    Text(
        text = annotatedString,
        fontSize = 13.sp,
        lineHeight = 20.sp
    )
}

@Preview
@Composable
fun ConsoleEmptyPreview() {
    AppTheme {
        ConsoleLine(
            text = "No output yet.",
            onJumpToLine = {}
        )
    }
}

@Preview
@Composable
fun ConsolePreview() {
    AppTheme {
        Console(
            outputLines = listOf(
                "Compiling script...",
                "Script.kts:5:12: error: unresolved reference: foo",
                "    foo()",
                "    ^^^",
                "Script.kts:10:5: warning: variable 'bar' is never used",
                "    val bar = 42",
                "    ^^^^^^^^^^",
                "Build failed with 1 error and 1 warning."
            ),
            onJumpToLine = { line -> println("Jump to line: $line") }
        )
    }
}