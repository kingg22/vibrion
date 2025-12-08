package io.github.kingg22.vibrion.domain.usecase.settings

import io.github.kingg22.vibrion.domain.repository.SettingsRepository

@JvmInline
value class LoadTokenUseCase(private val repository: SettingsRepository) {
    operator fun invoke() = repository.loadToken()
}
