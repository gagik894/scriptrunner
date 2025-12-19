package com.gagik.scriptrunner.ui.components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.gagik.scriptrunner.ui.theme.Dimens

/**
 * A responsive split layout that displays two panes separated by a draggable divider.
 *
 * - In compact mode (width < 800dp), panes are stacked vertically.
 * - In regular mode, panes are placed side by side horizontally.
 * - The divider allows resizing the panes, with the primary pane constrained
 *   between 20% and 80% of the available space.
 *
 * @param modifier Modifier applied to the root layout.
 * @param primaryContent Content shown in the primary pane.
 * @param secondaryContent Content shown in the secondary pane.
 */
@Composable
fun ResponsiveSplitLayout(
    modifier: Modifier = Modifier,
    primaryContent: @Composable () -> Unit,
    secondaryContent: @Composable () -> Unit
) {
    val density = LocalDensity.current
    var primaryWeight by remember { mutableStateOf(0.5f) }

    BoxWithConstraints(modifier = modifier) {
        val isCompact = maxWidth < Dimens.CompactBreakpoint

        val totalSizePx = with(density) {
            if (isCompact) maxHeight.toPx() else maxWidth.toPx()
        }

        val resizeLogic = { deltaPx: Float ->
            val weightDelta = deltaPx / totalSizePx
            primaryWeight = (primaryWeight + weightDelta).coerceIn(0.2f, 0.8f)
        }

        if (isCompact) {
            Column(Modifier.fillMaxSize()) {
                Box(Modifier.weight(primaryWeight)) { primaryContent() }

                DraggableDivider(
                    orientation = Orientation.Vertical,
                    onResize = resizeLogic
                )

                Box(Modifier.weight(1f - primaryWeight)) { secondaryContent() }
            }
        } else {
            Row(Modifier.fillMaxSize()) {
                Box(Modifier.weight(primaryWeight)) { primaryContent() }

                DraggableDivider(
                    orientation = Orientation.Horizontal,
                    onResize = resizeLogic
                )

                Box(Modifier.weight(1f - primaryWeight)) { secondaryContent() }
            }
        }
    }
}