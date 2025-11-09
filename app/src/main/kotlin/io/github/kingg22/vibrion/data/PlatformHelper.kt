package io.github.kingg22.vibrion.data

import io.github.kingg22.vibrion.core.application.DownloadForegroundService
import io.github.kingg22.vibrion.core.di.AndroidMusicStorage
import io.github.kingg22.vibrion.core.domain.model.Download
import io.github.kingg22.vibrion.core.domain.model.DownloadProvider
import android.content.Context as AndroidContext

class PlatformHelper(private val androidContext: AndroidContext) {
    fun enhanceDownload(download: Download): Download = download.copy(
        provider = download.provider + DownloadProvider.AndroidMusicStorage,
    )

    fun foregroundServiceRequired() {
        DownloadForegroundService.start(androidContext)
    }
}
