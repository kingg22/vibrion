package io.github.kingg22.vibrion.domain.usecase.search

import io.github.kingg22.vibrion.domain.repository.SearchRepository

@JvmInline
value class SearchSingleUseCase(private val repository: SearchRepository) : PagedSearchUseCase {
    suspend operator fun invoke(query: String, limit: Int) = repository.searchSingles(query, limit)
    override fun getPagedSearch(query: String) = repository.buildPagedSearchSingles(query)
}
