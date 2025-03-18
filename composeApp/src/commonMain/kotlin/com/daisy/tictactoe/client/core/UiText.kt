package com.daisy.tictactoe.client.core

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

sealed interface UiText {

    data class Plain(val value: String) : UiText

    data class Resource(
        val resId: StringResource,
        val args: List<Any> = emptyList()
    ) : UiText

    @Composable
    fun asString(): String {
        return when (this) {
            is Plain -> value
            is Resource -> stringResource(resId, *args.toTypedArray())
        }
    }
}