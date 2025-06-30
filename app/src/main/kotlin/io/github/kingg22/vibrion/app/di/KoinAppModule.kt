package io.github.kingg22.vibrion.app.di

import io.github.kingg22.vibrion.core.di.androidMusicStorageModule
import io.github.kingg22.vibrion.core.di.vibrionCoreModule
import io.github.kingg22.vibrion.ext.deezerId3Module
import org.koin.dsl.module

val appModule = module {
    includes(dataModule, uiModule)
    // External modules
    includes(vibrionCoreModule, androidMusicStorageModule, deezerId3Module)
}
