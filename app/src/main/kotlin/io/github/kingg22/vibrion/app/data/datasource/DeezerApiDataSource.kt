package io.github.kingg22.vibrion.app.data.datasource

import io.github.kingg22.deezer.client.api.DeezerApiClient
import io.github.kingg22.deezer.client.api.routes.SearchRoutes.Companion.buildAdvancedQuery
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

class DeezerApiDataSource(private val api: DeezerApiClient) {
    suspend fun searchSingle(query: String) = try {
        api.searches.search(buildAdvancedQuery(q = query))
    } catch (_: Exception) {
        currentCoroutineContext().ensureActive()
        null
    }
}
