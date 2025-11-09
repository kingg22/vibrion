package io.github.kingg22.vibrion.data.service

import co.touchlab.kermit.Logger
import io.github.kingg22.vibrion.core.application.DownloadOrchestrator
import io.github.kingg22.vibrion.data.datasource.music.DeezerGwDataSource
import io.github.kingg22.vibrion.domain.model.DownloadableAlbum
import io.github.kingg22.vibrion.domain.model.DownloadableItem
import io.github.kingg22.vibrion.domain.model.DownloadablePlaylist
import io.github.kingg22.vibrion.domain.model.DownloadableSingle
import io.github.kingg22.vibrion.domain.service.DownloadService

class DownloadServiceImpl(private val gwApi: DeezerGwDataSource, private val orchestrator: DownloadOrchestrator) :
    DownloadService {
    companion object {
        private val logger = Logger.withTag("DownloadService")
    }

    override suspend fun canDownload(token: String) = gwApi.canDownload(token)

    override suspend fun downloadItem(downloadable: DownloadableItem, token: String): Boolean = when (downloadable) {
        is DownloadableSingle -> gwApi.downloadSingle(downloadable, token)
        is DownloadableAlbum -> gwApi.downloadAlbum(downloadable, token)
        is DownloadablePlaylist -> gwApi.downloadPlaylist(downloadable, token)
    }

    override suspend fun cancelDownload(downloadableSingle: DownloadableItem): Boolean {
        logger.d { "cancelDownload: $downloadableSingle" }
        val state = orchestrator.cancelDownload(downloadableSingle.id)
        logger.d { "Orchestrator state cancel for ${downloadableSingle.id}: $state" }
        return state
    }
}
