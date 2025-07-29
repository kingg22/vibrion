package io.github.kingg22.vibrion.app.data.datasource

import io.github.kingg22.deezer.client.api.DeezerApiClient
import io.github.kingg22.deezer.client.api.routes.SearchRoutes.Companion.buildAdvancedQuery
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext

data class DeezerApiDataSource(private val api: DeezerApiClient) {
    suspend fun searchSingle(query: String) = try {
        api.searches.search(buildAdvancedQuery(q = query))
    } catch (_: Exception) {
        coroutineContext.ensureActive()
        null
    }
}
