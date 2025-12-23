package io.github.kingg22.vibrion.di

import io.github.kingg22.vibrion.data.service.AudioPlayerServiceNoOpImpl
import io.github.kingg22.vibrion.domain.service.AudioPlayerService
import org.koin.core.module.Module
import org.koin.dsl.module

actual val vibrionPlatformModule: Module
    get() = module {
        // TODO create impl of audio service for desktop
        single<AudioPlayerService> { _ ->
            AudioPlayerServiceNoOpImpl()
        }
    }
