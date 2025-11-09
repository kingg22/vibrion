package io.github.kingg22.vibrion

import android.app.Application
import co.touchlab.kermit.Logger
import co.touchlab.kermit.koin.KermitKoinLogger
import io.github.kingg22.vibrion.di.vibrionAppModule
import org.koin.android.ext.koin.androidContext
import org.koin.androix.startup.KoinStartup
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.KoinConfiguration

@OptIn(KoinExperimentalAPI::class)
class MainApplication :
    Application(),
    KoinStartup {

    override fun onKoinStartup() = KoinConfiguration {
        // Log Koin into Android logger
        logger(KermitKoinLogger(Logger.withTag("koin")))
        // Reference Android context
        androidContext(this@MainApplication)
        // Load modules
        modules(vibrionAppModule) // specific module for android
    }
}
