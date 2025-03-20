package com.daisy.tictactoe.client.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daisy.tictactoe.client.presentation.GameAction
import com.daisy.tictactoe.client.presentation.GameUiState
import com.daisy.tictactoe.client.presentation.components.ConnectionErrorContent
import com.daisy.tictactoe.client.presentation.components.GameContent
import com.daisy.tictactoe.client.presentation.components.InvitationContent

@Composable
actual fun TicTacToeScreenContent(
    state: GameUiState,
    onAction: (GameAction) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when {
            state.roomId == null -> {
                InvitationContent(
                    state = state,
                    onAction = onAction,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
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
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                )
            }

            else -> {
                GameContent(
                    state = state,
                    onAction = onAction,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 24.dp),
                )
            }
        }
    }
}
