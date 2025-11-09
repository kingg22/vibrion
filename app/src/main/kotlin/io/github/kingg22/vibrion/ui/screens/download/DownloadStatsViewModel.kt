package io.github.kingg22.vibrion.ui.screens.download

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.kingg22.vibrion.core.application.DownloadOrchestrator
import io.github.kingg22.vibrion.core.domain.model.DownloadEvent
import io.github.kingg22.vibrion.domain.model.DownloadItem
import io.github.kingg22.vibrion.domain.model.DownloadState
import io.github.kingg22.vibrion.domain.model.DownloadStatus
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class DownloadStatsViewModel(private val orchestrator: DownloadOrchestrator) : ViewModel() {
    private val _state = MutableStateFlow(DownloadState())
    val state = _state.asStateFlow()
    private var downloadList = listOf<DownloadItem>()

    val downloads = orchestrator.events
        .runningFold(downloadList) { downloads, event ->
            when (event) {
                is DownloadEvent.DownloadCancelled -> {
                    _state.update { currentState ->
                        currentState.copy(cancelledCount = currentState.cancelledCount + 1)
                    }

                    downloads.map {
                        if (it.id == event.download.id && it.status == DownloadStatus.IN_PROGRESS) {
                            it.copy(status = DownloadStatus.CANCELLED)
                        } else {
                            it
                        }
                    }
                }

                is DownloadEvent.DownloadCompleted -> {
                    _state.update { currentState ->
                        currentState.copy(successCount = currentState.successCount + 1)
                    }

                    downloads.map {
                        if (it.id == event.download.id && it.status == DownloadStatus.IN_PROGRESS) {
                            it.copy(status = DownloadStatus.COMPLETED)
                        } else {
                            it
                        }
                    }
                }

                is DownloadEvent.DownloadFailed -> {
                    _state.update { currentState ->
                        currentState.copy(failedCount = currentState.failedCount + 1)
                    }

                    downloads.map {
                        if (it.id == event.download.id && it.status == DownloadStatus.IN_PROGRESS) {
                            it.copy(status = DownloadStatus.FAILED)
                        } else {
                            it
                        }
                    }
                }

                is DownloadEvent.DownloadPaused -> {
                    _state.update { currentState ->
                        currentState.copy(pausedCount = currentState.pausedCount + 1)
                    }

                    downloads.map {
                        if (it.id == event.download.id && it.status == DownloadStatus.IN_PROGRESS) {
                            it.copy(status = DownloadStatus.PAUSED)
                        } else {
                            it
                        }
                    }
                }

                is DownloadEvent.DownloadProgress -> {
                    downloads.map {
                        if (it.id == event.download.id && it.status == DownloadStatus.IN_PROGRESS) {
                            it.copy(progress = event.progress)
                        } else {
                            it
                        }
                    }
                }

                is DownloadEvent.DownloadQueued -> {
                    _state.update { currentState ->
                        currentState.copy(totalCount = currentState.totalCount + 1)
                    }

                    if (downloads.none { it.id == event.download.id }) {
                        downloads + DownloadItem(
                            id = event.download.id,
                            fileName = event.download.fileName,
                            fileSize = event.download.totalSize.toString(),
                            status = DownloadStatus.IN_PROGRESS,
                            progress = 0f,
                        )
                    } else if (downloads.any { it.id == event.download.id }) {
                        downloads.map { item ->
                            if (item.id == event.download.id) {
                                item.copy(
                                    status = DownloadStatus.IN_PROGRESS,
                                    progress = 0f,
                                    fileSize = event.download.totalSize.toString(),
                                )
                            } else {
                                item
                            }
                        }
                    } else {
                        downloads
                    }
                }

                is DownloadEvent.DownloadStarted -> {
                    downloads.map {
                        if (it.id == event.download.id) {
                            it.copy(status = DownloadStatus.IN_PROGRESS)
                        } else {
                            it
                        }
                    }
                }

                DownloadEvent.DownloadNullEvent,
                is DownloadEvent.PreferencesUpdated,
                is DownloadEvent.DownloadPhaseChanged,
                -> downloads
            }
        }
        .onEach { downloadList = it }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000, 20_000), emptyList())

    fun resumeDownload() {
        // TODO connect repository to know all cancelled/failed downloads
    }

    fun resumeDownload(id: String) {
        viewModelScope.launch {
            // resume instead of retry to only resume failed/cancelled/paused downloads
            orchestrator.resumeDownload(id)
        }
    }

    fun cancelDownload() {
        viewModelScope.launch {
            orchestrator.cancelAllDownloads()
        }
    }

    fun cancelDownload(id: String) {
        viewModelScope.launch {
            orchestrator.cancelDownload(id)
        }
    }
}
