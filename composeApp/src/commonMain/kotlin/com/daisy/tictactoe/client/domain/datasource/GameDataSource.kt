package com.daisy.tictactoe.client.domain.datasource

import com.daisy.tictactoe.client.domain.model.Move
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.websocket.WebSocketSession

interface GameDataSource {

    suspend fun createRoom(): Result<String>

    suspend fun getGameSession(roomId: String): Result<DefaultClientWebSocketSession>

    suspend fun sendAction(session: WebSocketSession, action: Move): Result<Unit>
}