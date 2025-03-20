package com.daisy.tictactoe.client.data.datasource

import com.daisy.tictactoe.client.AppSecrets
import com.daisy.tictactoe.client.data.utils.runCatchingResult
import com.daisy.tictactoe.client.domain.datasource.GameDataSource
import com.daisy.tictactoe.client.domain.model.Move
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class KtorGameDataSourceImpl(
    private val client: HttpClient
) : GameDataSource {

    override suspend fun createRoom(): Result<String> {
        return runCatchingResult {
            client.post("/room/create") {
                contentType(ContentType.Application.Json)
            }
                .bodyAsText().let { responseBody ->
                    Json.parseToJsonElement(responseBody)
                        .jsonObject["roomId"]
                        ?.jsonPrimitive
                        ?.content
                        ?: throw IllegalStateException("Room ID not found in response")
                }
        }
    }

    override suspend fun getGameSession(roomId: String): Result<DefaultClientWebSocketSession> {
        return runCatchingResult {
            client.webSocketSession {
                url("${AppSecrets.wsUrl}/room/$roomId/game")
            }
        }
    }

    override suspend fun sendAction(session: WebSocketSession, action: Move): Result<Unit> {
        return runCatchingResult {
            session.outgoing.send(
                Frame.Text("move#${Json.encodeToString(action)}")
            )
        }
    }
}