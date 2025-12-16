package com.gagik.scriptrunner.ui.output

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gagik.scriptrunner.domain.models.RunState
import com.gagik.scriptrunner.ui.output.components.Console
import com.gagik.scriptrunner.ui.output.components.OutputHeader

@Composable
fun OutputPane(
    runState: RunState,
    exitCode: Int?,
    outputLines: List<String>,
    onJumpToLine: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        OutputHeader(
            runState = runState,
            exitCode = exitCode,
            modifier = Modifier.fillMaxWidth()
        )

        Console(
            outputLines = outputLines,
            onJumpToLine = onJumpToLine,
            modifier = Modifier.weight(1f)
        )
    }
}