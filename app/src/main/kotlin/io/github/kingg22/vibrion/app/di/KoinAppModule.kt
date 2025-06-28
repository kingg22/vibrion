package io.github.kingg22.vibrion.app.di

import io.github.kingg22.vibrion.core.di.vibrionCoreModule
import org.koin.dsl.module

val appModule = module {
    includes(dataModule, uiModule)
    // External modules
    includes(vibrionCoreModule)
}
