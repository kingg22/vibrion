package io.github.kingg22.vibrion.app.data

import io.github.kingg22.vibrion.app.data.datasource.PreferencesDataSource
import io.github.kingg22.vibrion.app.domain.repository.SettingsRepository

class SettingsRepositoryImpl(private val preferences: PreferencesDataSource) : SettingsRepository {
    override fun loadSettings() = preferences.loadSettings()
    override fun loadToken() = preferences.loadToken()
    override suspend fun getSettings() = preferences.getSettings()
    override suspend fun updateToken(token: String) = preferences.updateToken(token)
    override suspend fun updateDownloadPath(path: String) = preferences.updateDownloadPath(path)
    override suspend fun updateMaxConcurrentDownloads(count: Int) = preferences.updateMaxConcurrentDownloads(count)
}
