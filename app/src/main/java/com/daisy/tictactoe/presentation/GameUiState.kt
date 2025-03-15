package com.daisy.tictactoe.presentation

import com.daisy.tictactoe.core.UiState
import com.daisy.tictactoe.core.UiText
import com.daisy.tictactoe.domain.model.GameState

data class GameUiState(

    val roomId: String? = null,

    val gameState: GameState = GameState(),

    val isConnecting: Boolean = false,

    val error: UiText? = null,

    val isGameReady: Boolean = false,

    val message: UiText? = null
) : UiState
