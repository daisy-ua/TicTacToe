package com.daisy.tictactoe.domain.repository

import com.daisy.tictactoe.domain.model.GameState
import com.daisy.tictactoe.domain.model.Move
import kotlinx.coroutines.flow.Flow

interface GameRepository {

    fun getGameStateStream(): Flow<GameState>

    suspend fun sendAction(action: Move)

    suspend fun closeStream()
}