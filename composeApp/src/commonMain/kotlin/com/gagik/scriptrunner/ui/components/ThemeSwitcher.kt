package com.gagik.scriptrunner.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.gagik.scriptrunner.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Animated theme switcher button with sun/moon icons.
 */
@Composable
fun ThemeSwitcher(
    isDarkTheme: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = if (isDarkTheme) 0f else 180f,
        animationSpec = tween(durationMillis = 300),
        label = "ThemeRotation"
    )

    Box(
        modifier = modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onToggle() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
            contentDescription = if (isDarkTheme) "Switch to Light Mode" else "Switch to Dark Mode",
            tint = if (isDarkTheme) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary,
            modifier = Modifier
                .size(20.dp)
                .rotate(rotation)
        )
    }
}

@Preview
@Composable
fun ThemeSwitcherPreview() {
    var isDarkTheme by remember { mutableStateOf(true) }
    AppTheme {
        ThemeSwitcher(
            isDarkTheme = isDarkTheme,
            onToggle = { isDarkTheme = !isDarkTheme }
        )
    }
}