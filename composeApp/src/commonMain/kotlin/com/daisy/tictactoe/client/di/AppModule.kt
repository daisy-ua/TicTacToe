package com.daisy.tictactoe.client.di

import com.daisy.tictactoe.client.data.repository.KtorGameRepositoryImpl
import com.daisy.tictactoe.client.domain.repository.GameRepository
import com.daisy.tictactoe.client.presentation.GameViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module


val appModule = module {
    single {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println(message)
                    }
                }
                level = LogLevel.INFO
            }
            install(WebSockets)
//            defaultRequest {
////                url(AppSecrets.httpBaseUrl)
//            }
        }
    }
    singleOf(::KtorGameRepositoryImpl) bind GameRepository::class
    viewModelOf(::GameViewModel)
}