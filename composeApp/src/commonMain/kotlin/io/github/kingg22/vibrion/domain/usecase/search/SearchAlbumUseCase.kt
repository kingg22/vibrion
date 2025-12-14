package io.github.kingg22.vibrion.domain.usecase.search

import io.github.kingg22.vibrion.domain.repository.SearchRepository
import kotlin.jvm.JvmInline

@JvmInline
value class SearchAlbumUseCase(private val repository: SearchRepository) : PagedSearchUseCase {
    suspend operator fun invoke(query: String, limit: Int) = repository.searchAlbums(query, limit)
    override fun getPagedSearch(query: String) = repository.buildPagedSearchAlbums(query)
}
