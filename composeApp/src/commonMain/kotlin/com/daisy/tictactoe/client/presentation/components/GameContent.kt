package com.daisy.tictactoe.client.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.daisy.tictactoe.client.core.utils.LocalDimens
import com.daisy.tictactoe.client.presentation.GameAction
import com.daisy.tictactoe.client.presentation.GameUiState
import org.jetbrains.compose.resources.stringResource
import tictactoeclient.composeapp.generated.resources.Res
import tictactoeclient.composeapp.generated.resources.draw
import tictactoeclient.composeapp.generated.resources.new_game_starts_soon
import tictactoeclient.composeapp.generated.resources.player_turn
import tictactoeclient.composeapp.generated.resources.player_won


@Composable
fun GameContent(
    state: GameUiState,
    onAction: (GameAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val text = with(state.gameState) {
        if (isGameEnded) {
            (winnerPlayer?.let { winnerPlayer ->
                stringResource(Res.string.player_won, winnerPlayer)
            } ?: stringResource(Res.string.draw))
                .run {
                    this.plus(stringResource(Res.string.new_game_starts_soon))
                }
        } else stringResource(Res.string.player_turn, currentPlayer)
    }

    val dimens = LocalDimens.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimens.buttonSpacing)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.defaultMinSize(minHeight = 80.dp)
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TicTacToeBoard(
                gameState = state.gameState,
                onCellClicked = { row, col -> onAction(GameAction.MakeMove(row, col)) },
                modifier = Modifier
                    .aspectRatio(1f)
            )
        }
    }
}