package com.daisy.tictactoe.client.core.utils

import androidx.compose.ui.unit.Dp

interface Dimens {
    val buttonSpacing: Dp
}

expect object PlatformDimens : Dimens