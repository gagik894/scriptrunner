package com.gagik.scriptrunner.ui.editor

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gagik.scriptrunner.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EditorHeader(){
    Surface(
        tonalElevation = 2.dp
    ) {
      Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
      ){
          Text(
              text = "Script Runner",
              modifier = Modifier.weight(1f),
              style = MaterialTheme.typography.titleLarge,
              maxLines = 1,
              overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
          )
          RunStopButton(onClick = {}, runState = RunState.IDLE)
      }
    }
}

enum class RunState {
    IDLE,
    RUNNING,
    STOPPING,
}

@Composable
private fun RunStopButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    runState: RunState
) {
    val (icon, color, description) = when (runState) {
        RunState.IDLE -> Triple(Icons.Default.PlayArrow, Color.Green, "Run")
        RunState.RUNNING -> Triple(Icons.Default.Stop, Color.Red, "Stop")
        RunState.STOPPING -> Triple(Icons.Default.Stop, Color.Gray, "Stopping")
    }
    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled && runState != RunState.STOPPING
    ){
        Crossfade(targetState = icon) { targetIcon ->
            Icon(
                imageVector = targetIcon,
                contentDescription = description,
                tint = color
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RunStopButtonPreview() {
    AppTheme {
        var state by remember { mutableStateOf(RunState.IDLE) }
        RunStopButton(onClick = {
            state = when (state) {
                RunState.IDLE -> RunState.RUNNING
                RunState.RUNNING -> RunState.IDLE
                else -> RunState.IDLE
            }
        }, runState = state)
    }
}

@Preview(showBackground = true)
@Composable
fun EditorHeaderPreview(){
    AppTheme {
        EditorHeader()
    }
}