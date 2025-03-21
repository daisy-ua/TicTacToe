package com.daisy.tictactoe.client.presentation

import androidx.lifecycle.viewModelScope
import com.daisy.tictactoe.client.core.BaseViewModel
import com.daisy.tictactoe.client.core.UiText
import com.daisy.tictactoe.client.domain.model.Move
import com.daisy.tictactoe.client.domain.repository.GameRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import tictactoeclient.composeapp.generated.resources.Res
import tictactoeclient.composeapp.generated.resources.error_cannot_parse_room_id
import tictactoeclient.composeapp.generated.resources.error_connection_failed
import tictactoeclient.composeapp.generated.resources.error_invalid_room_id


class GameViewModel(
    private val repository: GameRepository,
) : BaseViewModel<GameUiState, GameAction, GameEffect>(GameUiState()) {

    init {
        observeOpponentConnection()
    }

    override fun onAction(action: GameAction) {
        when (action) {
            is GameAction.MakeMove -> action.apply { handleMove(row, col) }
            GameAction.CreateRoom -> createRoom()
            is GameAction.JoinRoom -> joinRoom(action.roomId)
        }
    }

    private fun createRoom() = viewModelScope.launch {
        val result = repository.createRoom()

        result
            .onSuccess {
                val roomId = result.getOrNull()
                updateState { copy(roomId = roomId) }

                if (roomId == null) {
                    setEffect {
                        GameEffect.ShowToast(
                            UiText.Resource(Res.string.error_cannot_parse_room_id)
                        )
                    }
                    return@launch
                }

                joinRoom(roomId)
            }
            .onFailure {
                setEffectOnFailure(it.message)
            }

    }

    private fun joinRoom(roomId: String) = viewModelScope.launch {
        updateState { copy(roomId = roomId, isConnecting = true) }

        repository.getGameStateStream(roomId)
            .catch {
                setEffect {
                    val message = it.message?.let {
                        UiText.Plain(
                            it
                        )
                    } ?: UiText.Resource(Res.string.error_connection_failed)

                    GameEffect.ShowToast(message)
                }

                updateState {
                    copy(
                        error = it.message?.let { UiText.Plain(it) }
                            ?: UiText.Resource(Res.string.error_connection_failed),
                        isConnecting = false
                    )
                }
            }
            .collect { result ->
                result
                    .onSuccess {
                        updateState { copy(gameState = it, isConnecting = false) }
                    }
                    .onFailure { error ->
                        val message = error.message?.let {
                            UiText.Plain(it)
                        } ?: UiText.Resource(Res.string.error_invalid_room_id)
                        setEffect { GameEffect.ShowToast(message) }
                        updateState {
                            copy(
                                isConnecting = false,
                                roomId = null
                            )
                        }
                    }
            }
    }

    private fun handleMove(row: Int, col: Int) {
        val isMoveInvalid = currentState.gameState.run {
            board[row][col] != null || isGameEnded
        }

        if (isMoveInvalid) return

        viewModelScope.launch {
            val result = repository.sendAction(Move(row, col))
            result
                .onFailure {
                    setEffect { GameEffect.ShowToast(UiText.Resource(Res.string.error_connection_failed)) }
                }
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
                            isWaitingForOpponent = true
                        )
                    }
                } else {
                    updateState {
                        copy(
                            isGameReady = true,
                            isWaitingForOpponent = false
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

    private fun setEffectOnFailure(message: String?) {
        setEffect { GameEffect.ShowToast(UiText.Plain(message ?: "Unknown Error")) }
    }
}