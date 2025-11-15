package io.github.kingg22.vibrion.domain.model

import kotlin.time.Duration

sealed interface DownloadableItem {
    val id: String
    val title: String
    val description: String?
    val thumbnailUrl: String?
    val releaseDate: String?
    val duration: Duration?
    val artists: List<ArtistInfo>
    val album: String?

    sealed interface ItemWithTracks {
        val tracks: List<DownloadableItem>
    }

    /** Sub–interfaz para items con URL pública de streaming */
    interface StreamableItem {
        val streamUrl: String
    }
}
