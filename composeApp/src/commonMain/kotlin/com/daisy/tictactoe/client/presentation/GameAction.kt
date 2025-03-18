package com.daisy.tictactoe.client.presentation

import com.daisy.tictactoe.client.core.UiAction


sealed interface GameAction : UiAction {

    data object CreateRoom : GameAction

    data class JoinRoom(val roomId: String) : GameAction

    data class MakeMove(val row: Int, val col: Int) : GameAction
}