package com.daisy.tictactoe.client.data.utils

suspend fun <T> runCatchingResult(block: suspend () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (e: Exception) {
        Result.failure(e)
    }
}