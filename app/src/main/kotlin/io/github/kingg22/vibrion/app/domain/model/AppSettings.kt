package io.github.kingg22.vibrion.app.domain.model

data class AppSettings(val token: String = "", val downloadPath: String = "", val maxConcurrentDownloads: Int = 1)
