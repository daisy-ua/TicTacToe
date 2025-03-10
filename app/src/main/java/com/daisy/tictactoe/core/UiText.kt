package com.daisy.tictactoe.core

import android.content.Context
import androidx.annotation.StringRes

sealed interface UiText {

    data class Plain(val value: String) : UiText

    data class Resource(
        @StringRes val resId: Int,
        val args: List<Any> = emptyList()
    ) : UiText

    fun asString(context: Context): String {
        return when (this) {
            is Plain -> value
            is Resource -> context.getString(resId, *args.toTypedArray())
        }
    }
}