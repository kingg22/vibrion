package io.github.kingg22.vibrion.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.kingg22.vibrion.domain.model.DownloadableItem
import io.github.kingg22.vibrion.domain.model.ModelType
import io.github.kingg22.vibrion.domain.model.ModelType.ARTIST
import io.github.kingg22.vibrion.domain.model.ModelType.GENRE
import io.github.kingg22.vibrion.domain.model.ModelType.USER
import io.github.kingg22.vibrion.domain.usecase.search.LoadDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(private val loadDetailUseCase: LoadDetailUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun loadDetail(type: ModelType, id: String) {
        if (type == ARTIST || type == USER || type == GENRE) return
        viewModelScope.launch {
            _uiState.update { UiState.Loading }
            val detail = loadDetailUseCase(type, id)
            if (detail != null) {
                _uiState.update { UiState.Success(detail) }
            } else {
                _uiState.value = UiState.Error
            }
        }
    }

    sealed interface UiState {
        data object Loading : UiState
        data class Success(val detail: DownloadableItem) : UiState
        data object Error : UiState
    }
}
