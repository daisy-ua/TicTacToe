package com.daisy.tictactoe.presentation

import androidx.lifecycle.viewModelScope
import com.daisy.tictactoe.core.BaseViewModel
import com.daisy.tictactoe.core.UiText
import com.daisy.tictactoe.domain.model.GameState
import com.daisy.tictactoe.domain.model.Move
import com.daisy.tictactoe.domain.repository.GameRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class GameViewModel(
    private val repository: GameRepository,
) : BaseViewModel<GameUiState, GameAction, GameEffect>(GameUiState()) {

    private val gameState: StateFlow<GameState> = repository
        .getGameStateStream()
        .onStart { updateState { copy(isConnecting = true) } }
        .onCompletion { updateState { copy(isConnecting = false) } }
        .catch {
            setEffect {
                GameEffect.ShowToast(
                    UiText.Plain(
                        it.message ?: "Connection failed"
                    )
                )
            }

            updateState { copy(error = it.message ?: "Connection failed with unknown error.") }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), GameState())

    init {
        updateGameState()

        observeOpponentConnection()
    }

    override fun onAction(action: GameAction) {
        when (action) {
            is GameAction.MakeMove -> action.apply { handleMove(row, col) }
        }
    }

    private fun handleMove(row: Int, col: Int) {
        val isMoveInvalid = currentState.gameState.run {
            board[row][col] != null || isGameEnded
        }

        if (isMoveInvalid) return

        viewModelScope.launch {
            repository.sendAction(Move(row, col))
        }
    }

    private fun updateGameState() = viewModelScope.launch {
        gameState.collect { state ->
            updateState { copy(gameState = state, isConnecting = false) }
        }
    }

    private fun observeOpponentConnection() {
        state
            .distinctUntilChangedBy { it.gameState.connectedPlayers }
            .filterNot { it.gameState.connectedPlayers.isEmpty() }
            .map { it.gameState.connectedPlayers.size < 2 }
            .onEach { isWaitingForOpponent ->
                if (isWaitingForOpponent) {
                    updateState {
                        copy(
                            isGameReady = false,
                            message = "Waiting for an opponent..."
                        )
                    }
                } else {
                    updateState {
                        copy(
                            isGameReady = true,
                            message = null
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            repository.closeStream()
        }
    }
}