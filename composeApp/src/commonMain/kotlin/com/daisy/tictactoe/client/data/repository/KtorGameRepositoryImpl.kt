package com.daisy.tictactoe.client.data.repository

import com.daisy.tictactoe.client.AppSecrets
import com.daisy.tictactoe.client.domain.model.GameState
import com.daisy.tictactoe.client.domain.model.Move
import com.daisy.tictactoe.client.domain.repository.GameRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
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
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class KtorGameRepositoryImpl(
    private val client: HttpClient
) : GameRepository {

    private var session: WebSocketSession? = null

    override suspend fun createRoom(): String? {
        return client.post("${AppSecrets.httpBaseUrl}/room/create") {
            contentType(ContentType.Application.Json)
        }
            .bodyAsText().let { responseBody ->
                val jsonResponse = Json.parseToJsonElement(responseBody).jsonObject
                jsonResponse["roomId"]?.jsonPrimitive?.content
            }
    }

    override fun getGameStateStream(roomId: String): Flow<GameState> {
        return flow {
            session = client.webSocketSession {
                url("${AppSecrets.wsUrl}/room/$roomId/game")
            }

            val gameStates = session!!
                .incoming
                .consumeAsFlow()
                .filterIsInstance<Frame.Text>()
                .mapNotNull {
                    Json.decodeFromString<GameState>(it.readText())
                }

            emitAll(gameStates)
        }
    }

    override suspend fun sendAction(action: Move) {
        session?.outgoing?.send(
            Frame.Text("move#${Json.encodeToString(action)}")
        )
    }

    override suspend fun closeStream() {
        session?.close()
        session = null
    }
}