package io.github.kingg22.vibrion.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import dev.drewhamilton.poko.Poko
import kotlin.time.Duration

@Stable
@Immutable
@Poko
class DownloadableSingle(
    override val id: String,
    override val title: String,
    override val description: String?,
    override val thumbnailUrl: String? = null,
    val mediaUrl: String? = null,
    override val releaseDate: String? = null,
    override val duration: Duration? = null,
    override val artists: List<ArtistInfo> = emptyList(),
    override val album: String? = null,
) : DownloadableItem {
    companion object {
        @OptIn(kotlin.uuid.ExperimentalUuidApi::class)
        @JvmStatic
        val previewDefault
            get() = DownloadableSingle(
                id = kotlin.uuid.Uuid.random().toString(),
                title = "Title",
                description = "Description",
                thumbnailUrl = DEFAULT_THUMBNAIL_URL,
            )
        const val DEFAULT_THUMBNAIL_URL =
            "https://img.freepik.com/premium-psd/music-icon-user-interface-element-3d-render-illustration_516938-1693.jpg"
    }
}
