package com.daisy.tictactoe.client.domain.repository

import com.daisy.tictactoe.client.domain.model.GameState
import com.daisy.tictactoe.client.domain.model.Move
import kotlinx.coroutines.flow.Flow

interface GameRepository {

    suspend fun createRoom(): String?

    fun getGameStateStream(roomId: String): Flow<GameState>

    suspend fun sendAction(action: Move)

    suspend fun closeStream()
}