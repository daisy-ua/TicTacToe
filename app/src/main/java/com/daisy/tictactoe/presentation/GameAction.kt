package com.daisy.tictactoe.presentation

import com.daisy.tictactoe.core.UiAction

sealed interface GameAction : UiAction {

    data object CreateRoom : GameAction

    data class JoinRoom(val roomId: String) : GameAction

    data class MakeMove(val row: Int, val col: Int) : GameAction
}