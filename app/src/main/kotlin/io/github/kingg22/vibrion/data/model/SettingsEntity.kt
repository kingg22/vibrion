package io.github.kingg22.vibrion.data.model

class SettingsEntity(
    val theme: String,
    val token: String?,
    val downloadPath: String,
    val maxConcurrentDownloads: Int,
    val preferredQuality: String,
    val useCompleteStream: Boolean,
)
