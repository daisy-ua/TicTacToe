package com.daisy.tictactoe.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.daisy.tictactoe.core.ObserveEffects
import com.daisy.tictactoe.core.utils.showToast
import com.daisy.tictactoe.domain.model.GameState
import com.daisy.tictactoe.domain.model.Player
import com.daisy.tictactoe.presentation.components.TicTacToeBoard
import com.daisy.tictactoe.ui.theme.TicTacToeTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun TicTacToeScreen(
    viewModel: GameViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveEffects(flow = viewModel.effect) { effect ->
        when (effect) {
            is GameEffect.ShowToast -> context.showToast(effect.message)
        }
    }

    TicTacToeScreenContent(
        state = state,
        onAction = { action ->
            viewModel.setAction(action)
        }
    )
}


@Composable
private fun TicTacToeScreenContent(
    state: GameUiState,
    onAction: (GameAction) -> Unit,
) {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isConnecting -> {
                    LoadingContent(
                        text = "Connecting to server...",
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                state.error != null -> {
                    ConnectionErrorContent(
                        errorMessage = state.error,
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center)
                    )
                }

                !state.isGameReady -> {
                    LoadingContent(
                        text = state.message ?: "Loading..",
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                else -> {
                    GameContent(
                        state = state,
                        onAction = onAction,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun ConnectionErrorContent(
    errorMessage: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun GameContent(
    state: GameUiState,
    onAction: (GameAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val text = with(state.gameState) {
        if (isGameEnded) {
            (winnerPlayer?.let { winnerPlayer ->
                "Player $winnerPlayer won!\n"
            } ?: "Draw!")
                .run {
                    this.plus("New game starts soon.")
                }
        } else "Player $currentPlayer turn"
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(50.dp))

        TicTacToeBoard(
            gameState = state.gameState,
            onCellClicked = { row, col -> onAction(GameAction.MakeMove(row, col)) },
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )
    }
}

@Composable
private fun LoadingContent(
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(.7f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LinearProgressIndicator(
            color = MaterialTheme.colorScheme.onSurface.copy(.7f)
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun TicTacToeScreenPreview() {
    TicTacToeTheme {
        TicTacToeScreenContent(
            state = GameUiState(
                gameState = GameState(
                    connectedPlayers = listOf(Player.O, Player.X),
                    winnerPlayer = Player.O
                )
            ),
            onAction = {}
        )
    }
}