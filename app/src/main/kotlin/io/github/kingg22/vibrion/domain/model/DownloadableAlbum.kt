package io.github.kingg22.vibrion.domain.model

import androidx.compose.runtime.Immutable
import kotlin.time.Duration

@Immutable
data class DownloadableAlbum(
    override val id: String,
    override val title: String,
    override val description: String?,
    override val thumbnailUrl: String?,
    override val releaseDate: String?,
    override val duration: Duration?,
    override val artists: List<ArtistInfo>,
    override val tracks: List<DownloadableSingle>,
    val upc: String?,
) : DownloadableItem,
    DownloadableItem.ItemWithTracks {
    override val album = title
}
