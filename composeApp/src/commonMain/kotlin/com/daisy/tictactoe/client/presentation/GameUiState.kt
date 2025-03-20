package com.daisy.tictactoe.client.presentation

import com.daisy.tictactoe.client.core.UiState
import com.daisy.tictactoe.client.core.UiText
import com.daisy.tictactoe.client.domain.model.GameState


data class GameUiState(

    val roomId: String? = null,

    val gameState: GameState = GameState(),

    val isConnecting: Boolean = false,

    val error: UiText? = null,

    val isGameReady: Boolean = false,

    val isWaitingForOpponent: Boolean = false,
) : UiState
