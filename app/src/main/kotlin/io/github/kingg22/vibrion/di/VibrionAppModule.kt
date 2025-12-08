package io.github.kingg22.vibrion.di

import org.koin.dsl.module

val vibrionAppModule = module {
    // -- Merge modules --
    // Data store
    includes(preferenceModule) // ONLY ONE DATA STORE
    // Data, domain, ui
    includes(dataModule, domainModule)
    includes(uiModule)
    // External packages
    // includes(vibrionCoreModule, deezerId3Module)
    // Platform
    includes(vibrionPlatformModule)
}
