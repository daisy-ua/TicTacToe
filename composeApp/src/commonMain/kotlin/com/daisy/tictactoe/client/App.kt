package com.daisy.tictactoe.client

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.daisy.tictactoe.client.presentation.TicTacToeScreen
import com.daisy.tictactoe.client.ui.theme.TicTacToeTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext

@Composable
@Preview
fun App() {
    KoinContext {
        TicTacToeTheme(darkTheme = true) {
            Surface(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)
            ) {
                TicTacToeScreen()
            }
        }
    }
}