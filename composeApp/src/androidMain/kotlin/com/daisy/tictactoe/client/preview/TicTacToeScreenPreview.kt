package com.daisy.tictactoe.client.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.daisy.tictactoe.client.domain.model.GameState
import com.daisy.tictactoe.client.domain.model.Player
import com.daisy.tictactoe.client.presentation.GameUiState
import com.daisy.tictactoe.client.presentation.screen.TicTacToeScreenContent
import com.daisy.tictactoe.client.ui.theme.TicTacToeTheme

@Preview
@Composable
fun TicTacToeScreenPreview() {
    TicTacToeTheme {
        TicTacToeScreenContent(
            state = GameUiState(
                roomId = null,

                isConnecting = false,

                error = null,

                isGameReady = true,

//                message = null,

                gameState = GameState(
                    connectedPlayers = listOf(Player.O, Player.X),
                )
            ),
            onAction = {}
        )
    }
}