package com.daisy.tictactoe.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Move(
    val row: Int,
    val col: Int,
)
