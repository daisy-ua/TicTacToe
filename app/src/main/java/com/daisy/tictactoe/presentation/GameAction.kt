package com.daisy.tictactoe.presentation

import com.daisy.tictactoe.core.UiAction

sealed interface GameAction : UiAction {

    data class MakeMove(val row: Int, val col: Int) : GameAction
}