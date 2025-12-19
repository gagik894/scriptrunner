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
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gagik.scriptrunner.presentation.models.ConsoleUiLine
import com.gagik.scriptrunner.ui.theme.AppTheme
import com.gagik.scriptrunner.ui.theme.getJetBrainsMonoFontFamily
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Console(
    outputLines: List<ConsoleUiLine>,
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
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize().padding(8.dp)
        ) {
            items(outputLines) { line ->
                ConsoleLine(
                    line = line,
                    onJumpToLine = onJumpToLine
                )
            }
        }
    }
}

@Composable
private fun ConsoleLine(
    line: ConsoleUiLine,
    onJumpToLine: (Int) -> Unit
) {
    val fontFamily = getJetBrainsMonoFontFamily()

    val annotatedString = buildAnnotatedString {
        if (line.type == ConsoleUiLine.Type.ERROR && line.linkRange != null && line.targetLineNumber != null) {

            val linkText = line.text.substring(line.linkRange)

            val link = LinkAnnotation.Clickable(
                tag = "error_link",
                styles = TextLinkStyles(
                    style = SpanStyle(
                        color = Color(0xFF569CD6),
                        textDecoration = TextDecoration.Underline
                    )
                ),
                linkInteractionListener = { onJumpToLine(line.targetLineNumber) }
            )

            withLink(link) {
                append(linkText)
            }

            val errorDescription = line.text.substring(line.linkRange.last + 1)
            withStyle(SpanStyle(color = Color(0xFFFF6B68))) {
                append(errorDescription)
            }

        } else {
            val baseColor = when (line.type) {
                ConsoleUiLine.Type.ERROR -> Color(0xFFFF6B68)
                ConsoleUiLine.Type.SYSTEM -> Color(0xFF888888)
                else -> Color(0xFFBBBBBB)
            }
            withStyle(SpanStyle(color = baseColor)) {
                append(line.text)
            }
        }
    }

    Text(
        text = annotatedString,
        fontSize = 13.sp,
        lineHeight = 20.sp,
        fontFamily = fontFamily
    )
}

@Preview
@Composable
fun ConsolePreview() {
    AppTheme {
        Console(
            outputLines = listOf(
                ConsoleUiLine(text = "Hello, World!", type = ConsoleUiLine.Type.NORMAL),
                ConsoleUiLine(
                    text = "This is a preview of the console output.",
                    type = ConsoleUiLine.Type.NORMAL
                ),
                ConsoleUiLine(
                    text = "This is an error message.",
                    type = ConsoleUiLine.Type.ERROR,
                    linkRange = 10..15,
                    targetLineNumber = 3
                ),
                ConsoleUiLine(text = "This is a system message.", type = ConsoleUiLine.Type.SYSTEM)
            ),
            onJumpToLine = { line -> println("Jump to line: $line") }
        )
    }
}