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
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextDecoration
import com.gagik.scriptrunner.presentation.models.ConsoleUiLine
import com.gagik.scriptrunner.ui.output.LocalConsoleDefaults
import com.gagik.scriptrunner.ui.theme.AppTheme
import com.gagik.scriptrunner.ui.theme.ConsoleTheme
import com.gagik.scriptrunner.ui.theme.getJetBrainsMonoFontFamily
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Console(
    outputLines: List<ConsoleUiLine>,
    onJumpToLine: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val consoleDefaults = LocalConsoleDefaults.current
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
            modifier = Modifier.fillMaxSize().padding(consoleDefaults.contentPadding)
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
    val defaults = LocalConsoleDefaults.current
    val fontFamily = getJetBrainsMonoFontFamily()
    val colors = ConsoleTheme.colors

    val annotatedString = buildAnnotatedString {
        if (line.type == ConsoleUiLine.Type.ERROR && line.linkRange != null && line.targetLineNumber != null) {

            val linkText = line.text.substring(line.linkRange)

            val link = LinkAnnotation.Clickable(
                tag = "error_link",
                styles = TextLinkStyles(
                    style = SpanStyle(
                        color = colors.link,
                        textDecoration = TextDecoration.Underline
                    )
                ),
                linkInteractionListener = { onJumpToLine(line.targetLineNumber) }
            )

            withLink(link) {
                append(linkText)
            }

            val errorDescription = line.text.substring(line.linkRange.last + 1)
            withStyle(SpanStyle(color = colors.stderr)) {
                append(errorDescription)
            }

        } else {
            val baseColor = when (line.type) {
                ConsoleUiLine.Type.ERROR -> colors.stderr
                ConsoleUiLine.Type.SYSTEM -> colors.system
                else -> colors.stdout
            }
            withStyle(SpanStyle(color = baseColor)) {
                append(line.text)
            }
        }
    }

    Text(
        text = annotatedString,
        fontSize = defaults.fontSize,
        lineHeight = defaults.lineHeight,
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