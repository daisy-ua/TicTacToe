package com.daisy.tictactoe.client

import com.daisy.tictactoe.BuildConfig

actual object AppSecrets {
    actual val httpBaseUrl: String
        get() = BuildConfig.HTTP_BASE_URL
    actual val wsUrl: String
        get() = BuildConfig.WS_URL
}