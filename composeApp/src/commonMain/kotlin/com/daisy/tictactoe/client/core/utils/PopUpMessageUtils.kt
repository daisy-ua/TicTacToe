package com.daisy.tictactoe.client.core.utils

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import com.daisy.tictactoe.client.core.UiText

suspend fun showPopUpMessage(
    message: UiText,
    snackbarHostState: SnackbarHostState,
    duration: SnackbarDuration = SnackbarDuration.Short
) {
    snackbarHostState.apply {
        currentSnackbarData?.dismiss()
        showSnackbar(
            message = message.asString(),
            duration = duration
        )
    }
}