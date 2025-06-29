package io.github.kingg22.vibrion.app.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import io.github.kingg22.vibrion.app.data.Crypto.decrypt
import io.github.kingg22.vibrion.app.data.Crypto.encrypt
import io.github.kingg22.vibrion.app.domain.model.AppSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest

data class PreferencesDataSource(private val dataStore: DataStore<Preferences>) {
    companion object {
        // Settings Keys
        private val TOKEN_KEY = byteArrayPreferencesKey("token")
        private val DOWNLOAD_PATH_KEY = stringPreferencesKey("download_path")
        private val MAX_CONCURRENT_DOWNLOADS_KEY = intPreferencesKey("max_concurrent_downloads")
    }

    // Settings Methods
    fun loadSettings() = dataStore.data.map { preferences ->
        AppSettings(
            token = preferences[TOKEN_KEY]?.decrypt(),
            downloadPath = preferences[DOWNLOAD_PATH_KEY] ?: AppSettings.DEFAULT_DOWNLOAD_PATH,
            maxConcurrentDownloads =
            preferences[MAX_CONCURRENT_DOWNLOADS_KEY] ?: AppSettings.DEFAULT_MAX_CONCURRENT_DOWNLOAD,
        )
    }

    fun loadToken() = dataStore.data.map { preferences -> preferences[TOKEN_KEY]?.decrypt() }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getSettings() = dataStore.data.mapLatest { preferences ->
        AppSettings(
            token = preferences[TOKEN_KEY]?.decrypt(),
            downloadPath = preferences[DOWNLOAD_PATH_KEY] ?: AppSettings.DEFAULT_DOWNLOAD_PATH,
            maxConcurrentDownloads =
            preferences[MAX_CONCURRENT_DOWNLOADS_KEY] ?: AppSettings.DEFAULT_MAX_CONCURRENT_DOWNLOAD,
        )
    }.first()

    suspend fun updateToken(token: String) {
        dataStore.edit { it[TOKEN_KEY] = token.encrypt() }
    }

    suspend fun updateDownloadPath(path: String) {
        dataStore.edit { it[DOWNLOAD_PATH_KEY] = path }
    }

    suspend fun updateMaxConcurrentDownloads(count: Int) {
        dataStore.edit { it[MAX_CONCURRENT_DOWNLOADS_KEY] = count }
    }
}
