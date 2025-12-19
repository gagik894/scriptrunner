package com.gagik.scriptrunner.ui.editor.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gagik.scriptrunner.ui.editor.LocalEditorDefaults
import com.gagik.scriptrunner.ui.theme.Dimens
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun LineNumberGutter(
    modifier: Modifier = Modifier,
    lineCount: Int,
    firstVisibleLineIndex: Int,
    visibleLineCount: Int,
    lineHeight: Dp,
    textStyle: TextStyle,
    onLineNumberClick: (Int) -> Unit,
    highlightedLine: Int? = null
) {
    val defaults = LocalEditorDefaults.current
    val endLineIndex = (firstVisibleLineIndex + visibleLineCount).coerceAtMost(lineCount)

    Layout(
        modifier = modifier.width(defaults.gutterWidth),
        content = {
            for (i in firstVisibleLineIndex until endLineIndex) {
                LineNumber(
                    lineNumber = i + 1,
                    lineHeight = lineHeight,
                    style = textStyle,
                    onClick = { onLineNumberClick(i + 1) },
                    isHighlighted = highlightedLine == i + 1
                )
            }
        }
    ) { measurables, constraints ->
        val itemHeightPx = lineHeight.roundToPx()
        val totalHeight = itemHeightPx * lineCount

        val placeables = measurables.map {
            it.measure(
                constraints.copy(
                    minHeight = itemHeightPx,
                    maxHeight = itemHeightPx
                )
            )
        }

        layout(constraints.maxWidth, totalHeight) {
            placeables.forEachIndexed { index, placeable ->
                val absoluteLineIndex = firstVisibleLineIndex + index
                val y = absoluteLineIndex * itemHeightPx
                placeable.placeRelative(0, y)
            }
        }
    }
}

@Composable
private fun LineNumber(
    lineNumber: Int,
    lineHeight: Dp,
    style: TextStyle,
    onClick: () -> Unit,
    isHighlighted: Boolean
) {
    val defaults = LocalEditorDefaults.current
    val backgroundColor by animateColorAsState(
        targetValue = if (isHighlighted) MaterialTheme.colorScheme.primaryContainer.copy(alpha = defaults.gutterHighlightAlpha) else Color.Transparent,
        animationSpec = tween(durationMillis = defaults.animationDurationMillis)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(lineHeight)
            .background(backgroundColor)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = lineNumber.toString(),
            style = style.copy(
                fontWeight = if (isHighlighted) FontWeight.Bold else style.fontWeight,
                color = if (isHighlighted) MaterialTheme.colorScheme.primary else style.color
            ),
            modifier = Modifier
                .align(androidx.compose.ui.Alignment.CenterEnd)
                .padding(end = Dimens.SpacerSmall),
            textAlign = TextAlign.End
        )
    }
}

@Preview
@Composable
fun LineNumberGutterPreview() {
    LineNumberGutter(
        lineCount = 100,
        firstVisibleLineIndex = 0,
        visibleLineCount = 20,
        lineHeight = 20.dp,
        textStyle = TextStyle.Default,
        onLineNumberClick = {}
    )
}
