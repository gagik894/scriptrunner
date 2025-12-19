package com.gagik.scriptrunner.ui.output.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.gagik.scriptrunner.domain.models.RunState
import com.gagik.scriptrunner.ui.output.LocalConsoleDefaults
import com.gagik.scriptrunner.ui.theme.AppTheme
import com.gagik.scriptrunner.ui.theme.ConsoleTheme
import com.gagik.scriptrunner.ui.theme.Dimens
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun OutputHeader(
    runState: RunState,
    exitCode: Int?,
    modifier: Modifier = Modifier
) {
    val colors = ConsoleTheme.colors
    val defaults = LocalConsoleDefaults.current

    Surface(
        tonalElevation = Dimens.HeaderElevation,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.HeaderPaddingHorizontal, vertical = Dimens.HeaderPaddingVertical),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Output",
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(Modifier.weight(1f))

            when (runState) {
                RunState.RUNNING -> {
                    Text(
                        text = "● Running...",
                        color = colors.running,
                        fontSize = defaults.statusFontSize,
                        fontWeight = FontWeight.Medium
                    )
                }
                RunState.STOPPING -> {
                    Text(
                        text = "Stopping...",
                        color = colors.stopping,
                        fontSize = defaults.statusFontSize
                    )
                }
                RunState.IDLE -> {
                    if (exitCode != null) {
                        val isSuccess = exitCode == 0
                        Text(
                            text = "Process finished with exit code $exitCode",
                            color = if (isSuccess) colors.success else colors.stderr,
                            fontSize = defaults.statusFontSize
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun OutputHeaderPreview() {
    AppTheme {
        OutputHeader(
            runState = RunState.IDLE,
            exitCode = 0,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun OutputHeaderRunningPreview() {
    AppTheme {
        OutputHeader(
            runState = RunState.RUNNING,
            exitCode = 0,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun OutputHeaderRunningErrorPreview() {
    AppTheme {
        OutputHeader(
            runState = RunState.IDLE,
            exitCode = 1,
            modifier = Modifier.fillMaxWidth()
        )
    }
}