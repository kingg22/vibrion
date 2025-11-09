package io.github.kingg22.vibrion.domain.service

import io.github.kingg22.vibrion.domain.model.DownloadableItem

interface DownloadService {
    suspend fun canDownload(token: String): Boolean
    suspend fun downloadItem(downloadable: DownloadableItem, token: String): Boolean
    suspend fun cancelDownload(downloadableSingle: DownloadableItem): Boolean
}
