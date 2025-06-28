package io.github.kingg22.vibrion.app.data

import io.github.kingg22.vibrion.app.domain.model.Single
import io.github.kingg22.vibrion.app.domain.repository.SearchRepository

data class SearchRepositoryImpl(private val api: DeezerApiDataSource) : SearchRepository {
    override suspend fun searchSingles(query: String, limit: Int): List<Single> = api.searchSingle(query)?.let {
        it.data.map { t -> t.toDomain() }
    } ?: emptyList()
}
