package com.daisy.tictactoe.client

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.daisy.tictactoe.client.core.utils.LocalDimens
import com.daisy.tictactoe.client.core.utils.PlatformDimens
import com.daisy.tictactoe.client.di.initKoin
import org.jetbrains.compose.resources.stringResource
import tictactoeclient.composeapp.generated.resources.Res
import tictactoeclient.composeapp.generated.resources.app_name
import java.awt.Dimension

fun main() = application {
    val state = rememberWindowState(
        width = 1000.dp,
        height = 800.dp,
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = stringResource(Res.string.app_name),
        state = state
    ) {
        window.minimumSize = Dimension(800, 600)

        initKoin()

        CompositionLocalProvider(LocalDimens provides PlatformDimens) {
            App()
        }
    }
}