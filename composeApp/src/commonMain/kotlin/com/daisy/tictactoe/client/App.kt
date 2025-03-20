package com.daisy.tictactoe.client

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.daisy.tictactoe.client.core.utils.PlatformDimens
import com.daisy.tictactoe.client.core.utils.localprovider.LocalDimens
import com.daisy.tictactoe.client.core.utils.localprovider.LocalSnackbarHostState
import com.daisy.tictactoe.client.presentation.screen.TicTacToeScreen
import com.daisy.tictactoe.client.ui.theme.TicTacToeTheme
import org.koin.compose.KoinContext

@Composable
fun App() {
    val snackbarHostState = remember { SnackbarHostState() }

    CompositionLocalProvider(
        values = arrayOf(
            LocalDimens provides PlatformDimens,
            LocalSnackbarHostState provides snackbarHostState
        )
    ) {
        KoinContext {
            TicTacToeTheme(darkTheme = true) {
                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)
                ) {
                    TicTacToeScreen()
                }
            }
        }
    }
}