package io.github.kingg22.vibrion.di

import io.github.kingg22.vibrion.core.di.androidMusicStorageModule
import org.koin.core.module.Module
import org.koin.dsl.module

val vibrionPlatformModule: Module
    get() = module {
        includes(androidMusicStorageModule)
    }
