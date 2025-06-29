package io.github.kingg22.vibrion.app.data.datasource

import io.github.kingg22.deezerSdk.api.DeezerApiClient
import io.github.kingg22.deezerSdk.api.routes.SearchRoutes
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext

data class DeezerApiDataSource(private val api: DeezerApiClient) {
    suspend fun searchSingle(query: String) = try {
        api.searches.search(SearchRoutes.Companion.buildAdvanceQuery(q = query))
    } catch (_: Exception) {
        coroutineContext.ensureActive()
        null
    }
}
