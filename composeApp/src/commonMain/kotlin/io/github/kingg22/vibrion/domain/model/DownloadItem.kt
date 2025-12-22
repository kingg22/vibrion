package io.github.kingg22.vibrion.domain.model

data class DownloadItem(
    val id: String,
    val fileName: String,
    val fileSize: String,
    val progress: Float,
    val status: DownloadStatus,
) {
    companion object {
        @kotlin.jvm.JvmField
        val EMPTY = DownloadItem(
            id = "",
            fileName = "",
            fileSize = "",
            progress = 0f,
            status = DownloadStatus.IN_PROGRESS,
        )

        @kotlin.jvm.JvmField
        val PREVIEW = DownloadItem(
            id = "1",
            fileName = "Preview",
            fileSize = "100 MB",
            progress = 0.5f,
            status = DownloadStatus.IN_PROGRESS,
        )
    }
}
