package com.daisy.tictactoe.client.core.utils.localprovider

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf

val LocalSnackbarHostState =
    compositionLocalOf<SnackbarHostState> { error("No Snackbar Host State") }