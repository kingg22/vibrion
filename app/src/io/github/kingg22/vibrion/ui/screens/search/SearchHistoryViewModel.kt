package io.github.kingg22.vibrion.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.kingg22.vibrion.domain.usecase.search.ClearSearchHistoryUseCase
import io.github.kingg22.vibrion.domain.usecase.search.GetSearchHistoryUseCase
import io.github.kingg22.vibrion.domain.usecase.search.SaveSearchQueryUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchHistoryViewModel(
    getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val saveSearchQueryUseCase: SaveSearchQueryUseCase,
    private val clearSearchHistoryUseCase: ClearSearchHistoryUseCase,
) : ViewModel() {
    val searchHistory = getSearchHistoryUseCase()
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    fun saveQuery(query: String) {
        viewModelScope.launch {
            saveSearchQueryUseCase(query)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            clearSearchHistoryUseCase()
        }
    }
}
