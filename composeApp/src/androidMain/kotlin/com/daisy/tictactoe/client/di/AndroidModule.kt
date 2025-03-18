package com.daisy.tictactoe.client.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

val androidModule = module {
    single { get<Context>().applicationContext }
}

fun initKoin(context: Context) {
    startKoin {
        androidLogger()
        androidContext(context)
        modules(appModule, androidModule)
    }
}
