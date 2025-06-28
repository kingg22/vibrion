package io.github.kingg22.vibrion.app.data

import io.github.kingg22.deezerSdk.api.DeezerApiClient
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext

data class DeezerApiDataSource(private val api: DeezerApiClient) {
    suspend fun searchSingle(query: String) = runCatching {
        api.searches.search(query)
    }.onFailure { coroutineContext.ensureActive() }.getOrNull()
}
