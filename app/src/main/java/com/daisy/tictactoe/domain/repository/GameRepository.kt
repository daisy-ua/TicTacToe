package com.daisy.tictactoe.domain.repository

import com.daisy.tictactoe.domain.model.GameState
import com.daisy.tictactoe.domain.model.Move
import kotlinx.coroutines.flow.Flow

interface GameRepository {

    suspend fun createRoom(): String?

    fun getGameStateStream(roomId: String): Flow<GameState>

    suspend fun sendAction(action: Move)

    suspend fun closeStream()
}