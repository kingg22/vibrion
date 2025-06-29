package io.github.kingg22.vibrion.app.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import io.github.kingg22.vibrion.app.domain.model.AppSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest

data class PreferencesDataSource(private val dataStore: DataStore<Preferences>) {
    companion object {
        // Search History Keys
        private val SEARCH_HISTORY_KEY = stringPreferencesKey("search_history")

        // Settings Keys
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val DOWNLOAD_PATH_KEY = stringPreferencesKey("download_path")
        private val MAX_CONCURRENT_DOWNLOADS_KEY = intPreferencesKey("max_concurrent_downloads")
    }

    // Search History Methods
    fun getSearchHistory() = dataStore.data.map { preferences ->
        preferences[SEARCH_HISTORY_KEY]?.split(",")?.filter { it.isNotBlank() } ?: emptyList()
    }

    suspend fun saveSearchQuery(query: String) {
        dataStore.edit { preferences ->
            val history = preferences[SEARCH_HISTORY_KEY]?.split(",")?.toMutableList() ?: mutableListOf()
            if (!history.contains(query)) {
                history.add(0, query)
                if (history.size > 25) history.removeAt(history.lastIndex)
                preferences[SEARCH_HISTORY_KEY] = history.joinToString(",")
            }
        }
    }

    suspend fun clearSearchHistory() {
        dataStore.edit { it[SEARCH_HISTORY_KEY] = "" }
    }

    // Settings Methods
    fun loadSettings() = dataStore.data.map { preferences ->
        AppSettings(
            token = preferences[TOKEN_KEY] ?: "",
            downloadPath = preferences[DOWNLOAD_PATH_KEY] ?: "",
            maxConcurrentDownloads = preferences[MAX_CONCURRENT_DOWNLOADS_KEY] ?: 3,
        )
    }

    fun loadToken() = dataStore.data.map { preferences -> preferences[TOKEN_KEY] }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getSettings() = dataStore.data.mapLatest { preferences ->
        AppSettings(
            token = preferences[TOKEN_KEY] ?: "",
            downloadPath = preferences[DOWNLOAD_PATH_KEY] ?: "",
            maxConcurrentDownloads = preferences[MAX_CONCURRENT_DOWNLOADS_KEY] ?: 3,
        )
    }.first()

    suspend fun updateToken(token: String) {
        dataStore.edit { it[TOKEN_KEY] = token }
    }

    suspend fun updateDownloadPath(path: String) {
        dataStore.edit { it[DOWNLOAD_PATH_KEY] = path }
    }

    suspend fun updateMaxConcurrentDownloads(count: Int) {
        dataStore.edit { it[MAX_CONCURRENT_DOWNLOADS_KEY] = count }
    }
}
