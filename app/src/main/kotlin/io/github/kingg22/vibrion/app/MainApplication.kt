package io.github.kingg22.vibrion.app

import android.app.Application
import io.github.kingg22.vibrion.app.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.androix.startup.KoinStartup
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.KoinConfiguration

@OptIn(KoinExperimentalAPI::class)
class MainApplication :
    Application(),
    KoinStartup {

    override fun onKoinStartup() = KoinConfiguration {
        modules(appModule)
        androidContext(this@MainApplication)
    }
}
