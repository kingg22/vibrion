package io.github.kingg22.vibrion.domain.repository

import io.github.kingg22.vibrion.domain.model.AppSettings
import io.github.kingg22.vibrion.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun loadToken(): Flow<String?>
    fun loadAppSettings(): Flow<AppSettings>
    suspend fun updateTheme(theme: ThemeMode)
    suspend fun updateToken(token: String)
    suspend fun updateDownloadPath(path: String)
    suspend fun updateMaxConcurrentDownloads(count: Int)
    suspend fun updatePreferredQuality(quality: String)
    suspend fun updateUseCompleteStream(use: Boolean)
}
