package io.github.kingg22.vibrion.domain.model

data class DownloadState(
    val totalCount: Int = 0,
    val successCount: Int = 0,
    val failedCount: Int = 0,
    val cancelledCount: Int = 0,
    val pausedCount: Int = 0,
)
