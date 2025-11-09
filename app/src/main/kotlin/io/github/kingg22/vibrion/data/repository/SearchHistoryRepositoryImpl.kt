package io.github.kingg22.vibrion.data.repository

import io.github.kingg22.vibrion.data.datasource.preferences.PreferencesDataSource
import io.github.kingg22.vibrion.domain.model.SearchHistoryItem
import io.github.kingg22.vibrion.domain.model.VibrionPage
import io.github.kingg22.vibrion.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.time.ExperimentalTime

@JvmInline
@OptIn(ExperimentalTime::class)
value class SearchHistoryRepositoryImpl(private val preferencesDataSource: PreferencesDataSource) :
    SearchHistoryRepository {

    override fun getHistory() = preferencesDataSource.getSearchHistory().map { queries ->
        queries.reversed().map { SearchHistoryItem(it) }
    }

    override suspend fun saveSearch(query: String) {
        preferencesDataSource.saveSearchQuery(query)
    }

    override suspend fun clearHistory() {
        preferencesDataSource.clearSearchHistory()
    }

    override suspend fun loadPage(key: Int?, size: Int): VibrionPage<Int, SearchHistoryItem> {
        val pageIndex = key ?: 0
        val allItems = getHistory().first()
        val fromIndex = pageIndex * size

        if (fromIndex >= allItems.size) return VibrionPage(emptyList(), prevKey = null, nextKey = null)

        val toIndex = minOf(fromIndex + size, allItems.size)
        val page = allItems.subList(fromIndex, toIndex)

        val prevKey = if (pageIndex == 0) null else pageIndex - 1
        val nextKey = if (toIndex < allItems.size) pageIndex + 1 else null

        return VibrionPage(
            data = page,
            prevKey = prevKey,
            nextKey = nextKey,
        )
    }
}
