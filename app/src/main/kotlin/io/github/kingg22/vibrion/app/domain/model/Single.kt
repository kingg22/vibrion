package io.github.kingg22.vibrion.app.domain.model

import kotlin.time.Duration

data class Single(
    val id: String? = null,
    val title: String,
    val description: String?,
    val thumbnailUrl: String? = null,
    val mediaUrl: String? = null,
    val releaseDate: String? = null,
    val duration: Duration? = null,
    val artists: List<Artist>? = null,
    val explicit: Boolean? = null,
) {
    companion object {
        const val DEFAULT_THUMBNAIL_URL =
            "https://img.freepik.com/premium-psd/music-icon-user-interface-element-3d-render-illustration_516938-1693.jpg"
    }
}
