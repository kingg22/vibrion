package io.github.kingg22.vibrion.app.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

class SearchViewModel : ViewModel() {
    val searchResults: StateFlow<List<ResultItemData>> = flowOf(
        List(5) {
            ResultItemData("", "", "", "", "")
        },
    ).stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun search(query: String) {
        // TODO
    }
}
