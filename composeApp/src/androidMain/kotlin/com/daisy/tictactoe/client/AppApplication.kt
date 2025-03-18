package com.daisy.tictactoe.client

import android.app.Application
import com.daisy.tictactoe.client.di.initKoin
import org.koin.core.component.KoinComponent


class AppApplication : Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()

        initKoin(this@AppApplication)
    }
}