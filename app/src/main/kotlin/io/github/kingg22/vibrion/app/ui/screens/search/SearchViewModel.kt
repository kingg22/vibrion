package io.github.kingg22.vibrion.app.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.kingg22.vibrion.app.domain.model.Single
import io.github.kingg22.vibrion.app.domain.repository.SearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration

data class SearchViewModel(private val repository: SearchRepository) : ViewModel() {
    private val _searchResults = MutableStateFlow<SearchResultUiState>(SearchResultUiState.Idle)
    val searchResults = _searchResults.asStateFlow()

    fun search(query: String) {
        _searchResults.update { SearchResultUiState.Loading }
        viewModelScope.launch {
            val results = repository.searchSingles(query)
            _searchResults.update { _ ->
                if (results.isEmpty()) return@update SearchResultUiState.Error("No results found")
                SearchResultUiState.Success(
                    results.map {
                        val description = if (!it.artists.isNullOrEmpty()) {
                            it.artists.joinToString(", ") { artist -> artist.name }
                        } else {
                            it.description ?: it.title
                        }

                        it.copy(
                            thumbnailUrl = it.thumbnailUrl ?: Single.DEFAULT_THUMBNAIL_URL,
                            title = it.title,
                            description = description,
                            releaseDate = it.releaseDate ?: "-",
                            duration = it.duration ?: Duration.ZERO,
                        )
                    },
                )
            }
        }
    }

    fun modifyState(state: SearchResultUiState) {
        _searchResults.update { state }
    }

    fun download(item: Single) {
        viewModelScope.launch {
            // TODO
        }
    }

    sealed interface SearchResultUiState {
        data object Idle : SearchResultUiState
        data object Loading : SearchResultUiState
        data class Success(val results: List<Single>) : SearchResultUiState
        data class Error(val message: String) : SearchResultUiState
    }
}
