package io.github.kingg22.vibrion.domain.model

import androidx.compose.runtime.Immutable
import kotlin.time.Duration

@Immutable
data class DownloadablePlaylist(
    override val id: String,
    override val title: String,
    override val description: String?,
    override val thumbnailUrl: String?,
    override val releaseDate: String?,
    override val duration: Duration?,
    private val creator: ArtistInfo?,
    override val tracks: List<DownloadableSingle>,
) : DownloadableItem,
    DownloadableItem.ItemWithTracks {
    override val artists: List<ArtistInfo> = listOfNotNull(creator)
    override val album: String? = null
}
