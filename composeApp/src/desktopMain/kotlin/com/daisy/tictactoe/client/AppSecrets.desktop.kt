package com.daisy.tictactoe.client

actual object AppSecrets {
    actual val httpBaseUrl: String
        get() = ApiKeyProperties.httpBaseUrl
    actual val wsUrl: String
        get() = ApiKeyProperties.wsUrl
}