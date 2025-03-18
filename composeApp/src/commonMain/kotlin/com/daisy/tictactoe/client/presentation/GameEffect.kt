package com.daisy.tictactoe.client.presentation

import com.daisy.tictactoe.client.core.UiEffect
import com.daisy.tictactoe.client.core.UiText


sealed interface GameEffect : UiEffect {

    data class ShowToast(val message: UiText) : GameEffect
}