package io.github.kingg22.vibrion.data.service

import io.github.kingg22.vibrion.domain.model.DownloadableItem
import io.github.kingg22.vibrion.domain.service.DownloadService

class DownloadServiceNoOpImpl : DownloadService {
    override suspend fun canDownload(token: String): Boolean = false
    override suspend fun downloadItem(downloadable: DownloadableItem, token: String): Boolean = false
    override suspend fun cancelDownload(downloadableSingle: DownloadableItem): Boolean = false
}
