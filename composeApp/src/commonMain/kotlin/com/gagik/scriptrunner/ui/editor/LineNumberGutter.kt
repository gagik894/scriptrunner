package com.gagik.scriptrunner.ui.editor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun LineNumberGutter(
    modifier: Modifier = Modifier,
    lineCount: Int,
    firstVisibleLineIndex: Int,
    visibleLineCount: Int,
    lineHeight: Dp,
    onLineNumberClick: (Int) -> Unit
) {
    val endLineIndex =
        (firstVisibleLineIndex + visibleLineCount).coerceAtMost(lineCount)

    Layout(
        modifier = modifier.width(40.dp),
        content = {
            for (i in firstVisibleLineIndex until endLineIndex) {
                LineNumber(
                    lineNumber = i + 1,
                    lineHeight = lineHeight,
                    onClick = { onLineNumberClick(i + 1) }
                )
            }
        }
    ) { measurables, constraints ->

        val itemHeightPx = lineHeight.roundToPx()
        val visibleHeightPx = itemHeightPx * (endLineIndex - firstVisibleLineIndex)

        val placeables = measurables.map {
            it.measure(
                constraints.copy(
                    minHeight = itemHeightPx,
                    maxHeight = itemHeightPx
                )
            )
        }

        layout(constraints.maxWidth, visibleHeightPx) {
            placeables.forEachIndexed { index, placeable ->
                placeable.placeRelative(0, index * itemHeightPx)
            }
        }
    }
}

@Composable
private fun LineNumber(
    lineNumber: Int,
    lineHeight: Dp,
    onClick: () -> Unit
) {
    Text(
        text = lineNumber.toString(),
        color = Color.Gray,
        modifier = Modifier
            .fillMaxWidth()
            .height(lineHeight)
            .clickable(onClick = onClick)
            .padding(end = 4.dp),
        textAlign = TextAlign.End
    )
}

@Preview
@Composable
fun LineNumberGutterPreview() {
    LineNumberGutter(
        lineCount = 100,
        firstVisibleLineIndex = 9,
        visibleLineCount = 15,
        lineHeight = 20.dp,
        onLineNumberClick = {}
    )
}
