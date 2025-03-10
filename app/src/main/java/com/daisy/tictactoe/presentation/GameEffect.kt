package com.daisy.tictactoe.presentation

import com.daisy.tictactoe.core.UiEffect
import com.daisy.tictactoe.core.UiText

sealed interface GameEffect : UiEffect {

    data class ShowToast(val message: UiText) : GameEffect
}