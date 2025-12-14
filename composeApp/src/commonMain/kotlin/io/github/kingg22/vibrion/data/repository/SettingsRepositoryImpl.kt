package io.github.kingg22.vibrion.data.repository

import io.github.kingg22.vibrion.data.datasource.preferences.PreferencesDataSource
import io.github.kingg22.vibrion.domain.model.AppSettings
import io.github.kingg22.vibrion.domain.model.ThemeMode
import io.github.kingg22.vibrion.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(private val preferencesDataSource: PreferencesDataSource) : SettingsRepository {
    override fun loadToken() = preferencesDataSource.loadToken()

    override fun loadAppSettings() = preferencesDataSource.loadSettings().map { entity ->
        AppSettings(
            theme = ThemeMode.valueOf(entity.theme),
            token = entity.token,
            downloadPath = entity.downloadPath,
            maxConcurrentDownloads = entity.maxConcurrentDownloads,
            preferredQuality = entity.preferredQuality,
            useCompleteStream = entity.useCompleteStream,
        )
    }

    override suspend fun updateTheme(theme: ThemeMode) {
        preferencesDataSource.updateTheme(theme.name)
    }

    override suspend fun updateToken(token: String) {
        preferencesDataSource.updateToken(token)
    }

    override suspend fun updateDownloadPath(path: String) {
        preferencesDataSource.updateDownloadPath(path)
    }

    override suspend fun updateMaxConcurrentDownloads(count: Int) {
        preferencesDataSource.updateMaxConcurrentDownloads(count)
    }

    override suspend fun updatePreferredQuality(quality: String) {
        preferencesDataSource.updatePreferredQuality(quality)
    }

    override suspend fun updateUseCompleteStream(use: Boolean) {
        preferencesDataSource.updateUseCompleteStream(use)
    }
}
