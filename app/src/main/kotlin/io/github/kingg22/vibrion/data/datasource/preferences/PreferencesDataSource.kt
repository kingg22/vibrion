package io.github.kingg22.vibrion.data.datasource.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import co.touchlab.kermit.Logger
import io.github.kingg22.vibrion.data.Crypto
import io.github.kingg22.vibrion.data.model.SettingsEntity
import io.github.kingg22.vibrion.domain.model.AppSettings
import io.github.kingg22.vibrion.domain.model.Quality
import io.github.kingg22.vibrion.domain.model.ThemeMode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest

@JvmInline
value class PreferencesDataSource(private val dataStore: DataStore<Preferences>) {
    companion object {
        private val logger = Logger.withTag("PreferencesDataSource")

        // Search History Keys
        private val SEARCH_HISTORY_KEY = stringSetPreferencesKey("search_history_set")

        // Settings Keys
        private val THEME_KEY = stringPreferencesKey("theme")
        private val TOKEN_KEY = byteArrayPreferencesKey("token")
        private val DOWNLOAD_PATH_KEY = stringPreferencesKey("download_path")
        private val MAX_CONCURRENT_DOWNLOADS_KEY = intPreferencesKey("max_concurrent_downloads")
        private val PREFERRED_QUALITY_KEY = stringPreferencesKey("preferred_quality")
        private val USE_COMPLETE_STREAM_KEY = booleanPreferencesKey("use_complete_stream")
    }

    // Search History Methods
    fun getSearchHistory() = dataStore.data.map { preferences ->
        preferences[SEARCH_HISTORY_KEY] ?: emptySet()
    }

    suspend fun saveSearchQuery(query: String) {
        dataStore.edit { preferences ->
            val history = preferences[SEARCH_HISTORY_KEY]?.toMutableSet() ?: mutableSetOf()
            if (history.add(query)) {
                preferences[SEARCH_HISTORY_KEY] = if (history.size > 25) {
                    history.toList().subList(history.size.coerceIn(0, 25), history.size).toSet()
                } else {
                    history
                }
            }
        }
    }

    suspend fun clearSearchHistory() {
        dataStore.edit { it[SEARCH_HISTORY_KEY] = emptySet() }
    }

    // Settings Methods
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getSettings() = dataStore.data.mapLatest(::mapSettings).first()

    fun loadSettings() = dataStore.data.map(::mapSettings)

    fun loadToken() = dataStore.data.map { preferences -> preferences[TOKEN_KEY]?.decrypt() }

    private suspend inline fun mapSettings(preferences: Preferences) = SettingsEntity(
        theme = preferences[THEME_KEY] ?: ThemeMode.SYSTEM.name,
        token = preferences[TOKEN_KEY]?.decrypt(),
        downloadPath = preferences[DOWNLOAD_PATH_KEY] ?: AppSettings.DEFAULT_DOWNLOAD_PATH,
        maxConcurrentDownloads =
        preferences[MAX_CONCURRENT_DOWNLOADS_KEY] ?: AppSettings.DEFAULT_MAX_CONCURRENT_DOWNLOAD,
        preferredQuality = preferences[PREFERRED_QUALITY_KEY] ?: Quality.AUTO.name,
        useCompleteStream = preferences[USE_COMPLETE_STREAM_KEY] == true,
    )

    suspend fun updateTheme(theme: String) {
        dataStore.edit { it[THEME_KEY] = theme }
    }

    suspend fun updateToken(token: String) {
        val encryptedToken = token.encrypt()
        dataStore.edit { it[TOKEN_KEY] = encryptedToken }
    }

    suspend fun updateDownloadPath(path: String) {
        dataStore.edit { it[DOWNLOAD_PATH_KEY] = path }
    }

    suspend fun updateMaxConcurrentDownloads(count: Int) {
        dataStore.edit { it[MAX_CONCURRENT_DOWNLOADS_KEY] = count }
    }

    suspend fun updatePreferredQuality(quality: String) {
        dataStore.edit { it[PREFERRED_QUALITY_KEY] = quality }
    }

    suspend fun updateUseCompleteStream(use: Boolean) {
        dataStore.edit { it[USE_COMPLETE_STREAM_KEY] = use }
    }

    private suspend inline fun ByteArray.decrypt() = try {
        Crypto.decrypt(this)
    } catch (e: Exception) {
        logger.e(e) { "Problem to decrypt the token, going to clean the current token, fallback to empty string" }
        dataStore.edit { it[TOKEN_KEY] = "".encrypt() }
        ""
    }

    private fun String.encrypt() = try {
        Crypto.encrypt(this)
    } catch (e: Exception) {
        logger.e(e) { "Problem to encrypt the token, fallback to empty byte array" }
        "".toByteArray()
    }
}
