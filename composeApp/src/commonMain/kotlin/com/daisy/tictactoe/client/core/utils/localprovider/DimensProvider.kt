package com.daisy.tictactoe.client.core.utils.localprovider

import androidx.compose.runtime.compositionLocalOf
import com.daisy.tictactoe.client.core.utils.Dimens

val LocalDimens = compositionLocalOf<Dimens> { error("No Dimens provided") }