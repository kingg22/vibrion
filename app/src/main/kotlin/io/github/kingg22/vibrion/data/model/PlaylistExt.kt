package io.github.kingg22.vibrion.data.model

import io.github.kingg22.deezer.client.api.objects.ImageSizes
import io.github.kingg22.deezer.client.api.objects.Playlist
import io.github.kingg22.deezer.client.api.objects.retrieveImageUrl
import io.github.kingg22.deezer.client.api.objects.withImageSize
import io.github.kingg22.vibrion.domain.model.ArtistInfo
import io.github.kingg22.vibrion.domain.model.DownloadablePlaylist
import kotlin.time.Duration.Companion.seconds

fun Playlist.toDomain() = DownloadablePlaylist(
    id = this.id.toString(),
    title = this.title,
    description = null,
    thumbnailUrl = pictureBig ?: md5Image?.let { retrieveImageUrl().withImageSize(ImageSizes.BIG) },
    releaseDate = creationDate?.toReadableString(),
    duration = this.duration?.seconds,
    creator = ArtistInfo(id = creator.id.toString(), name = creator.name, pictureUrl = creator.pictureBig),
    tracks = tracks?.data?.map { it.toDomain() } ?: emptyList(),
)
