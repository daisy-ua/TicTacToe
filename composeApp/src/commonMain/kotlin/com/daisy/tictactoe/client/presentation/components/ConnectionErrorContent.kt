package com.daisy.tictactoe.client.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.daisy.tictactoe.client.core.UiText


@Composable
fun ConnectionErrorContent(
    errorMessage: UiText,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = errorMessage.asString(),
            color = MaterialTheme.colorScheme.error
        )
    }
}