package io.github.kingg22.vibrion.domain.usecase.settings

import io.github.kingg22.vibrion.domain.model.ThemeMode
import io.github.kingg22.vibrion.domain.repository.SettingsRepository

@JvmInline
value class UpdateSettingsUseCase(private val repository: SettingsRepository) {
    suspend fun updateTheme(theme: ThemeMode) = repository.updateTheme(theme)
    suspend fun updateToken(token: String) = repository.updateToken(token)
    suspend fun updateDownloadPath(path: String) = repository.updateDownloadPath(path)
    suspend fun updateMaxConcurrentDownloads(count: Int) = repository.updateMaxConcurrentDownloads(count)
    suspend fun updatePreferredQuality(quality: String) = repository.updatePreferredQuality(quality)
    suspend fun updateUseCompleteStream(use: Boolean) = repository.updateUseCompleteStream(use)
}
