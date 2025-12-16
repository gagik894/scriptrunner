package com.gagik.scriptrunner.ui.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun AppVerticalScrollbar(
    scrollState: ScrollState,
    modifier: Modifier
) {
    // No-op on Android (Standard mobile behavior)
}

@Composable
actual fun AppVerticalScrollbar(
    scrollState: LazyListState,
    modifier: Modifier
) {
    // No-op on Android
}