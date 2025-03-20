package com.daisy.tictactoe.client.data.repository

import com.daisy.tictactoe.client.data.utils.runCatchingResult
import com.daisy.tictactoe.client.domain.datasource.GameDataSource
import com.daisy.tictactoe.client.domain.model.GameState
import com.daisy.tictactoe.client.domain.model.Move
import com.daisy.tictactoe.client.domain.repository.GameRepository
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.json.Json

class KtorGameRepositoryImpl(
    private val dataSource: GameDataSource
) : GameRepository {

    private var session: WebSocketSession? = null

    override suspend fun createRoom(): Result<String> {
        return dataSource.createRoom()
    }

    override fun getGameStateStream(roomId: String): Flow<Result<GameState>> {
        return flow {
            session = dataSource.getGameSession(roomId).also { result ->
                if (result.isFailure) {
                    emit(Result.failure(result.exceptionOrNull()!!))
                    return@flow
                }
            }.getOrNull()

            val gameStates = session!!
                .incoming
                .consumeAsFlow()
                .filterIsInstance<Frame.Text>()
                .mapNotNull { frame ->
                    runCatchingResult {
                        Json.decodeFromString<GameState>(frame.readText())
                    }
                }

            emitAll(gameStates)

            val closeReason = (session as DefaultClientWebSocketSession).closeReason.await()
            if (closeReason?.code != CloseReason.Codes.NORMAL.code) {
                emit(Result.failure(Throwable(closeReason?.message)))
            }
        }
    }

    override suspend fun sendAction(action: Move): Result<Unit> {
        return session?.let { currentSession ->
            dataSource.sendAction(currentSession, action)
        } ?: Result.failure(IllegalStateException("WebSocket session not established"))
    }

    override suspend fun closeStream() {
        session?.close()
        session = null
    }
}