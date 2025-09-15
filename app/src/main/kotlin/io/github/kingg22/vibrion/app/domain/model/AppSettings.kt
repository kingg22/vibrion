package io.github.kingg22.vibrion.app.domain.model

class AppSettings(
    val token: String? = null,
    val downloadPath: String = DEFAULT_DOWNLOAD_PATH,
    val maxConcurrentDownloads: Int = DEFAULT_MAX_CONCURRENT_DOWNLOAD,
) {
    companion object {
        const val DEFAULT_MAX_CONCURRENT_DOWNLOAD = 3
        const val DEFAULT_DOWNLOAD_PATH = ""
    }
}
