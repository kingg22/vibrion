package io.github.kingg22.vibrion.domain.usecase.search

import io.github.kingg22.vibrion.domain.repository.SearchRepository

@JvmInline
value class SearchArtistUseCase(private val repository: SearchRepository) {
    suspend operator fun invoke(query: String, limit: Int) = repository.searchArtists(query, limit)

    fun getPagedSearch(query: String) = repository.buildPagedSearchArtists(query)
}
