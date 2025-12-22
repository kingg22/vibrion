package io.github.kingg22.vibrion.domain.model

class AppSettings(
    val theme: ThemeMode,
    val token: String?,
    val downloadPath: String,
    val maxConcurrentDownloads: Int,
    val preferredQuality: String,
    val useCompleteStream: Boolean,
) {
    companion object {
        @kotlin.jvm.JvmField
        val default = AppSettings(
            theme = ThemeMode.SYSTEM,
            token = null,
            downloadPath = "",
            maxConcurrentDownloads = DEFAULT_MAX_CONCURRENT_DOWNLOAD,
            preferredQuality = Quality.AUTO.name,
            useCompleteStream = false,
        )
        const val DEFAULT_MAX_CONCURRENT_DOWNLOAD = 3
        const val DEFAULT_DOWNLOAD_PATH = ""
    }
}
