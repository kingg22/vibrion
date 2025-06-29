package io.github.kingg22.vibrion.app.domain.repository

import io.github.kingg22.vibrion.app.domain.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun loadSettings(): Flow<AppSettings>
    fun loadToken(): Flow<String?>
    suspend fun getSettings(): AppSettings
    suspend fun updateToken(token: String)
    suspend fun updateDownloadPath(path: String)
    suspend fun updateMaxConcurrentDownloads(count: Int)
}
