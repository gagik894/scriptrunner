package com.gagik.scriptrunner.ui.editor

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gagik.scriptrunner.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EditorHeader(
    modifier: Modifier = Modifier,
    runState: RunState,
    supportedLanguages: List<String>,
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onRunClick: () -> Unit,
    onStopClick: () -> Unit,
) {
    Surface(
        tonalElevation = 4.dp,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
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
                supportedLanguages = supportedLanguages,
                onLanguageSelected = onLanguageSelected,
                selectedLanguage = selectedLanguage,
            )
            Spacer(Modifier.width(16.dp))
            RunStopButton(
                runState = runState,
                onRunClick = onRunClick,
                onStopClick = onStopClick
            )
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
    runState: RunState,
    onRunClick: () -> Unit,
    onStopClick: () -> Unit
) {
    val (icon, containerColor, label) = when (runState) {
        RunState.IDLE -> Triple(Icons.Default.PlayArrow, MaterialTheme.colorScheme.primary, "Run")
        RunState.RUNNING -> Triple(Icons.Default.Stop, MaterialTheme.colorScheme.error, "Stop")
        RunState.STOPPING -> Triple(Icons.Default.Stop, Color.Gray, "Stopping")
    }

    Button(
        onClick = if (runState == RunState.RUNNING) onStopClick else onRunClick,
        enabled = runState != RunState.STOPPING,
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
        modifier = modifier.height(36.dp)
    ) {
        Crossfade(targetState = icon) { targetIcon ->
            Icon(
                imageVector = targetIcon,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(text = label)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguageSelector(
    supportedLanguages: List<String>,
    onLanguageSelected: (String) -> Unit,
    selectedLanguage: String,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .clickable { expanded = true }
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedLanguage,
                style = MaterialTheme.typography.labelLarge,
            )
            Spacer(Modifier.width(4.dp))
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
            supportedLanguages.forEach { language ->
                DropdownMenuItem(
                    text = { Text(language) },
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
            supportedLanguages = listOf("Kotlin", "Java", "JavaScript"),
            onLanguageSelected = {},
            selectedLanguage = "Kotlin"
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
            supportedLanguages = listOf("Kotlin", "Java", "JavaScript"),
            selectedLanguage = "Kotlin",
            onLanguageSelected = {},
            onRunClick = {},
            onStopClick = {}
        )
    }
}