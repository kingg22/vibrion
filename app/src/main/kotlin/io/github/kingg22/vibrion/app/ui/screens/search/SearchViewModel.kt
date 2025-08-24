package io.github.kingg22.vibrion.app.ui.screens.search

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.kingg22.deezer.client.gw.DeezerGwClient
import io.github.kingg22.deezer.client.gw.requests.MediaRequest.Companion.toMediaRequest
import io.github.kingg22.deezer.client.utils.HttpClientBuilder
import io.github.kingg22.deezer.client.utils.UnofficialDeezerApi
import io.github.kingg22.vibrion.app.domain.model.Single
import io.github.kingg22.vibrion.app.domain.repository.SearchRepository
import io.github.kingg22.vibrion.app.domain.repository.SettingsRepository
import io.github.kingg22.vibrion.core.application.DownloadForegroundService
import io.github.kingg22.vibrion.core.application.DownloadOrchestrator
import io.github.kingg22.vibrion.core.di.AndroidMusicStorage
import io.github.kingg22.vibrion.core.domain.model.DownloadProvider
import io.github.kingg22.vibrion.ext.DeezerId3
import io.github.kingg22.vibrion.ext.toMusicMetadata
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration

@OptIn(UnofficialDeezerApi::class)
data class SearchViewModel(
    private val repository: SearchRepository,
    private val settingsRepository: SettingsRepository,
    private val orchestrator: DownloadOrchestrator,
    private val httpClientBuilder: HttpClientBuilder,
) : ViewModel() {
    private val token = settingsRepository.loadToken()
    private var canDownload: Boolean = false
    private val _searchResults = MutableStateFlow<SearchResultUiState>(SearchResultUiState.Idle)
    val searchResults = _searchResults.asStateFlow()
    private lateinit var gwClient: DeezerGwClient

    init {
        viewModelScope.launch {
            token.collect {
                canDownload = it != null && DeezerGwClient.verifyArl(it, httpClientBuilder.copy())
            }
        }
    }

    fun canDownload() = canDownload

    fun search(query: String) {
        _searchResults.update { SearchResultUiState.Loading }
        viewModelScope.launch {
            val results = repository.searchSingles(query)
            _searchResults.update { _ ->
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

    fun download(item: Single, context: Context) {
        if (!canDownload) return
        checkNotNull(item.id)
        viewModelScope.launch {
            token.filterNotNull().collectLatest { arl ->
                if (!::gwClient.isInitialized) gwClient = DeezerGwClient.initialize(arl, httpClientBuilder.copy())
                val track = gwClient.tracks.getTrackData(item.id.toLong())
                val mediaResult = gwClient.getMedias(track.trackToken.toMediaRequest())
                val urls = mediaResult.getAllUrls()
                check(urls.isNotEmpty())
                DownloadForegroundService.start(context)
                val download = orchestrator.createDownload(
                    urls.first(),
                    item.title,
                    "vibrion/${item.title}",
                    DownloadProvider.DeezerId3 + DownloadProvider.AndroidMusicStorage,
                    DownloadProvider.deezerProcessMetadata(item.id.toLong()),
                    track.toTrack().toMusicMetadata(),
                )
                Log.i("SearchViewModel", "Download enqueue: ${download.getOrNull()}")
            }
        }
    }

    sealed interface SearchResultUiState {
        data object Idle : SearchResultUiState
        data object Loading : SearchResultUiState
        data class Success(val results: List<Single>) : SearchResultUiState
        data class Error(val message: String) : SearchResultUiState
    }
}
