package com.daisy.tictactoe.client.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Move(
    val row: Int,
    val col: Int,
)