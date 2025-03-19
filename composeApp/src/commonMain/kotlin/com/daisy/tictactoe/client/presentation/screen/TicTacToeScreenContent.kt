package com.daisy.tictactoe.client.presentation.screen

import androidx.compose.runtime.Composable
import com.daisy.tictactoe.client.presentation.GameAction
import com.daisy.tictactoe.client.presentation.GameUiState

@Composable
expect fun TicTacToeScreenContent(
    state: GameUiState,
    onAction: (GameAction) -> Unit
)