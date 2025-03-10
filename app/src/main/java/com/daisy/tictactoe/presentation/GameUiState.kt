package com.daisy.tictactoe.presentation

import com.daisy.tictactoe.core.UiState
import com.daisy.tictactoe.domain.model.GameState

data class GameUiState(

    val gameState: GameState = GameState(),

    val isConnecting: Boolean = false,

    val error: String? = null,

    val isGameReady: Boolean = false,

    val message: String? = null
) : UiState
