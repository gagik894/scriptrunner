package com.gagik.scriptrunner.ui.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun AppVerticalScrollbar(
    scrollState: ScrollState,
    modifier: Modifier = Modifier
)

@Composable
expect fun AppVerticalScrollbar(
    scrollState: LazyListState,
    modifier: Modifier = Modifier
)