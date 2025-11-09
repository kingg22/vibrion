package io.github.kingg22.vibrion.domain.usecase.search

import io.github.kingg22.vibrion.domain.model.DownloadableItem
import io.github.kingg22.vibrion.domain.repository.PagedSource

interface PagedSearchUseCase {
    fun getPagedSearch(query: String): PagedSource<Int, DownloadableItem>
}
