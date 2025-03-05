package com.daisy.tictactoe.navigation

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object Game : Route
}