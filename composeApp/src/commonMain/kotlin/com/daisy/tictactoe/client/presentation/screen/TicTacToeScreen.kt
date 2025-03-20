package com.daisy.tictactoe.client.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.daisy.tictactoe.client.core.ObserveEffects
import com.daisy.tictactoe.client.core.utils.localprovider.LocalSnackbarHostState
import com.daisy.tictactoe.client.core.utils.showPopUpMessage
import com.daisy.tictactoe.client.presentation.GameEffect
import com.daisy.tictactoe.client.presentation.GameViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TicTacToeScreen(
    viewModel: GameViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = LocalSnackbarHostState.current
    val scope = rememberCoroutineScope()

    ObserveEffects(flow = viewModel.effect, snackbarHostState) { effect ->
        when (effect) {
            is GameEffect.ShowToast -> {
                scope.launch {
                    showPopUpMessage(
                        message = effect.message,
                        snackbarHostState = snackbarHostState
                    )
                }
            }
        }
    }

    TicTacToeScreenContent(
        state = state,
        onAction = { action ->
            viewModel.setAction(action)
        }
    )
}