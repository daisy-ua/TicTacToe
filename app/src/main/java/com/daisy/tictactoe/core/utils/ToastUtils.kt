package com.daisy.tictactoe.core.utils

import android.content.Context
import android.widget.Toast
import com.daisy.tictactoe.core.UiText

fun Context.showToast(
    message: UiText,
    duration: Int = Toast.LENGTH_LONG,
) {
    Toast.makeText(this, message.asString(this), duration).show()
}