package com.gagik.scriptrunner.ui.components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gagik.scriptrunner.ui.modifiers.resizeCursor

@Composable
fun DraggableDivider(
    orientation: Orientation,
    onResize: (Float) -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.outline,
    thickness: Dp = 1.dp,
    touchTargetSize: Dp = 12.dp
) {
    val isVerticalDrag = orientation == Orientation.Vertical

    Box(
        modifier = modifier
            .then(
                if (isVerticalDrag) Modifier.fillMaxWidth().height(touchTargetSize)
                else Modifier.fillMaxHeight().width(touchTargetSize)
            )
            .resizeCursor(orientation)
            .draggable(
                state = rememberDraggableState { delta -> onResize(delta) },
                orientation = orientation
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isVerticalDrag) {
            HorizontalDivider(thickness = thickness, color = color)
        } else {
            VerticalDivider(thickness = thickness, color = color, modifier = Modifier.fillMaxHeight())
        }
    }
}