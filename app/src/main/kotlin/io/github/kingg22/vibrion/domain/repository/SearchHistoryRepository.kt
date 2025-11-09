package io.github.kingg22.vibrion.domain.repository

import io.github.kingg22.vibrion.domain.model.SearchHistoryItem
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository : PagedSource<Int, SearchHistoryItem> {
    fun getHistory(): Flow<List<SearchHistoryItem>>
    suspend fun saveSearch(query: String)
    suspend fun clearHistory()
}
