package com.daisy.tictactoe.client.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.daisy.tictactoe.client.core.ObserveEffects
import com.daisy.tictactoe.client.core.utils.showMessage
import com.daisy.tictactoe.client.presentation.GameEffect
import com.daisy.tictactoe.client.presentation.GameViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TicTacToeScreen(
    viewModel: GameViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveEffects(flow = viewModel.effect) { effect ->
        when (effect) {
            is GameEffect.ShowToast -> showMessage(effect.message)
        }
    }

    TicTacToeScreenContent(
        state = state,
        onAction = { action ->
            viewModel.setAction(action)
        }
    )
}