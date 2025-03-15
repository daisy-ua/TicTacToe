package com.daisy.tictactoe.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.daisy.tictactoe.R
import com.daisy.tictactoe.core.ObserveEffects
import com.daisy.tictactoe.core.UiText
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
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun InvitationContent(
    state: GameUiState,
    onAction: (GameAction) -> Unit,
    modifier: Modifier = Modifier
) {
    var passcode by remember {
        mutableStateOf(state.roomId ?: "")
    }

    val context = LocalContext.current

    Surface(
        modifier = modifier,
    ) {
        when {
            state.isConnecting -> {
                LoadingContent(
                    text = stringResource(R.string.connecting_to_server),
                    modifier = Modifier.fillMaxSize(),
                )
            }

            else -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.invite_with_code),
                        style = MaterialTheme.typography.headlineMedium
                    )

                    OutlinedTextField(
                        value = passcode,
                        onValueChange = {
                            passcode = it
                        },
                        enabled = state.message == null,
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(if (state.message == null) 1f else 0f),
                    ) {
                        OutlinedButton(
                            onClick = { onAction(GameAction.CreateRoom) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            Text(text = stringResource(id = R.string.create_invitation))
                        }

                        Button(
                            onClick = { onAction(GameAction.JoinRoom(passcode)) },
                            enabled = passcode.isNotEmpty(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            Text(text = stringResource(id = R.string.battle))
                        }
                    }

                    state.message?.let {
                        Text(
                            text = it.asString(context),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ConnectionErrorContent(
    errorMessage: UiText,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = errorMessage.asString(context),
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
                stringResource(id = R.string.player_won, winnerPlayer)
            } ?: stringResource(id = R.string.draw))
                .run {
                    this.plus(stringResource(id = R.string.new_game_starts_soon))
                }
        } else stringResource(id = R.string.player_turn, currentPlayer)
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f)
                .padding(top = 36.dp)
        )

        TicTacToeBoard(
            gameState = state.gameState,
            onCellClicked = { row, col -> onAction(GameAction.MakeMove(row, col)) },
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .weight(2f)
        )

        Spacer(
            modifier = Modifier
                .height(50.dp)
                .weight(1f)
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
                roomId = null,

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