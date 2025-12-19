package com.gagik.scriptrunner.ui.editor.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gagik.scriptrunner.domain.models.RunState
import com.gagik.scriptrunner.domain.models.ScriptLanguage
import com.gagik.scriptrunner.ui.theme.AppTheme
import com.gagik.scriptrunner.ui.theme.ConsoleTheme
import com.gagik.scriptrunner.ui.theme.Dimens
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EditorHeader(
    modifier: Modifier = Modifier,
    runState: RunState,
    selectedLanguage: ScriptLanguage,
    onLanguageSelected: (ScriptLanguage) -> Unit,
    onRunClick: () -> Unit,
    onStopClick: () -> Unit,
) {
    Surface(
        tonalElevation = Dimens.HeaderElevation,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.HeaderPaddingHorizontal, vertical = Dimens.HeaderPaddingVertical),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Script Runner",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            LanguageSelector(
                onLanguageSelected = onLanguageSelected,
                selectedLanguage = selectedLanguage,
            )
            Spacer(Modifier.width(Dimens.SpacerLarge))
            RunStopButton(
                runState = runState,
                onRunClick = onRunClick,
                onStopClick = onStopClick
            )
        }
    }
}



@Composable
private fun RunStopButton(
    modifier: Modifier = Modifier,
    runState: RunState,
    onRunClick: () -> Unit,
    onStopClick: () -> Unit
) {
    val colors = ConsoleTheme.colors
    val (icon, containerColor, label) = when (runState) {
        RunState.IDLE -> Triple(Icons.Default.PlayArrow, MaterialTheme.colorScheme.primary, "Run")
        RunState.RUNNING -> Triple(Icons.Default.Stop, MaterialTheme.colorScheme.error, "Stop")
        RunState.STOPPING -> Triple(Icons.Default.Stop, colors.stopping, "Stopping")
    }

    Button(
        onClick = if (runState == RunState.RUNNING) onStopClick else onRunClick,
        enabled = runState != RunState.STOPPING,
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        contentPadding = PaddingValues(horizontal = Dimens.HeaderPaddingHorizontal, vertical = 0.dp),
        modifier = modifier.height(Dimens.ButtonHeight)
    ) {
        Crossfade(targetState = icon) { targetIcon ->
            Icon(
                imageVector = targetIcon,
                contentDescription = null,
                modifier = Modifier.size(Dimens.IconSizeSmall)
            )
        }
        Spacer(Modifier.width(Dimens.SpacerMedium))
        Text(text = label)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguageSelector(
    onLanguageSelected: (ScriptLanguage) -> Unit,
    selectedLanguage: ScriptLanguage,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(Dimens.CornerRadius))
                .clickable { expanded = true }
                .padding(horizontal = Dimens.SpacerMedium, vertical = Dimens.SpacerSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedLanguage.name,
                style = MaterialTheme.typography.labelLarge,
            )
            Spacer(Modifier.width(Dimens.SpacerSmall))
            val rotation by animateFloatAsState(
                targetValue = if (expanded) 180f else 0f,
                label = "ArrowRotation"
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Select Language",
                modifier = Modifier.rotate(rotation)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            ScriptLanguage.entries.forEach { language ->
                DropdownMenuItem(
                    text = { Text(language.name) },
                    onClick = {
                        expanded = false
                        onLanguageSelected(language)
                    },
                    colors = if (language == selectedLanguage) {
                        MenuDefaults.itemColors(
                            textColor = MaterialTheme.colorScheme.primary,
                            leadingIconColor = MaterialTheme.colorScheme.primary
                        )
                    } else MenuDefaults.itemColors()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LanguageSelectorPreview() {
    AppTheme {
        LanguageSelector(
            onLanguageSelected = {},
            selectedLanguage = ScriptLanguage.KOTLIN
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RunStopButtonPreview() {
    AppTheme {
        var state by remember { mutableStateOf(RunState.IDLE) }
        RunStopButton(
            runState = state,
            onRunClick = { state = RunState.RUNNING },
            onStopClick = { state = RunState.IDLE }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditorHeaderPreview() {
    AppTheme {
        EditorHeader(
            runState = RunState.IDLE,
            selectedLanguage = ScriptLanguage.KOTLIN,
            onLanguageSelected = {},
            onRunClick = {},
            onStopClick = {}
        )
    }
}