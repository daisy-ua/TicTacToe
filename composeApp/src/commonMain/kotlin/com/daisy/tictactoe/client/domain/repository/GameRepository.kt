package com.daisy.tictactoe.client.domain.repository

import com.daisy.tictactoe.client.domain.model.GameState
import com.daisy.tictactoe.client.domain.model.Move
import kotlinx.coroutines.flow.Flow

interface GameRepository {

    suspend fun createRoom(): Result<String>

    fun getGameStateStream(roomId: String): Flow<Result<GameState>>

    suspend fun sendAction(action: Move): Result<Unit>

    suspend fun closeStream()
}