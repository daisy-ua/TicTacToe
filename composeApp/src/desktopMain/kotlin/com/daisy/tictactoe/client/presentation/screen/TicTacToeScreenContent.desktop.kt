package com.daisy.tictactoe.client.presentation.screen

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daisy.tictactoe.client.domain.model.GameState
import com.daisy.tictactoe.client.domain.model.Player
import com.daisy.tictactoe.client.presentation.GameAction
import com.daisy.tictactoe.client.presentation.GameUiState
import com.daisy.tictactoe.client.presentation.components.ConnectionErrorContent
import com.daisy.tictactoe.client.presentation.components.GameContent
import com.daisy.tictactoe.client.presentation.components.InvitationContent
import com.daisy.tictactoe.client.ui.theme.TicTacToeTheme

@Composable
actual fun TicTacToeScreenContent(
    state: GameUiState,
    onAction: (GameAction) -> Unit
) {
    Scaffold(
        backgroundColor = MaterialTheme.colorScheme.surface,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when {
                state.roomId == null -> {
                    InvitationContent(
                        state = state,
                        onAction = onAction,
                        modifier = Modifier
                            .defaultMinSize(minWidth = 450.dp)
                            .fillMaxSize(.5f)
                            .padding(horizontal = 48.dp)
                    )
                }

                state.error != null ->
                    ConnectionErrorContent(
                        errorMessage = state.error,
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center)
                    )

                !state.isGameReady -> {
                    InvitationContent(
                        state = state,
                        onAction = onAction,
                        modifier = Modifier
                            .defaultMinSize(minWidth = 450.dp)
                            .fillMaxSize(.5f)
                            .padding(horizontal = 48.dp)
                    )
                }

                else -> {
                    GameContent(
                        state = state,
                        onAction = onAction,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(48.dp)
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun TicTacToeScreenContentPreview() {
    TicTacToeTheme {
        TicTacToeScreenContent(
            state = GameUiState(
                roomId = "null",

                isConnecting = false,

                error = null,

                isGameReady = true,

                message = null,

                gameState = GameState(
                    connectedPlayers = listOf(Player.O, Player.X),
                )
            ),
            onAction = {}
        )
    }
}