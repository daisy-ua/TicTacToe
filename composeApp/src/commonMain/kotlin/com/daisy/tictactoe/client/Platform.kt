package com.daisy.tictactoe.client

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform