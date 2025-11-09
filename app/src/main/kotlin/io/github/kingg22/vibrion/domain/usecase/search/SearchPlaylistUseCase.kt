package io.github.kingg22.vibrion.domain.usecase.search

import io.github.kingg22.vibrion.domain.repository.SearchRepository

@JvmInline
value class SearchPlaylistUseCase(private val repository: SearchRepository) : PagedSearchUseCase {
    suspend operator fun invoke(query: String, limit: Int) = repository.searchPlaylists(query, limit)
    override fun getPagedSearch(query: String) = repository.buildPagedSearchPlaylists(query)
}
