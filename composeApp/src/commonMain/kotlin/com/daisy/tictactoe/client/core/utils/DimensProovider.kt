package com.daisy.tictactoe.client.core.utils

import androidx.compose.runtime.compositionLocalOf

val LocalDimens = compositionLocalOf<Dimens> { error("No Dimens provided") }