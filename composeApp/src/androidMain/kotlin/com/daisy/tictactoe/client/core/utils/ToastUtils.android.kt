package com.daisy.tictactoe.client.core.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import com.daisy.tictactoe.client.core.UiText
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object AndroidToastProvider : KoinComponent {
    private val context: Context by inject()

    fun showMessage(message: UiText) {
//        Toast.makeText(context, message.asString(), Toast.LENGTH_LONG).show()
        println(message)
    }
}

actual fun showMessage(message: UiText) {
    AndroidToastProvider.showMessage(message)
}