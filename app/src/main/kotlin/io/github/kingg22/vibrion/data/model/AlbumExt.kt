package io.github.kingg22.vibrion.data.model

import io.github.kingg22.deezer.client.api.objects.Album
import io.github.kingg22.deezer.client.api.objects.ImageSizes
import io.github.kingg22.deezer.client.api.objects.retrieveImageUrl
import io.github.kingg22.deezer.client.api.objects.withImageSize
import io.github.kingg22.vibrion.domain.model.ArtistInfo
import io.github.kingg22.vibrion.domain.model.DownloadableAlbum
import kotlin.time.Duration.Companion.seconds

fun Album.toDomain() = DownloadableAlbum(
    id = this.id.toString(),
    title = this.title,
    description = null,
    thumbnailUrl = coverBig ?: md5Image?.let { retrieveImageUrl().withImageSize(ImageSizes.BIG) },
    releaseDate = this.releaseDate?.toReadableString(),
    duration = this.duration?.seconds,
    artists = contributors?.map { contributor ->
        ArtistInfo(
            id = contributor.id.toString(),
            name = contributor.name,
            pictureUrl = contributor.pictureBig,
        )
    }?.takeIf { l -> l.isNotEmpty() } ?: run {
        artist?.let { artist ->
            listOf(
                ArtistInfo(
                    id = artist.id.toString(),
                    name = artist.name,
                    pictureUrl = artist.pictureBig,
                ),
            )
        } ?: emptyList()
    },
    tracks = this.tracks?.data?.map { it.toDomain() } ?: emptyList(),
    upc = this.upc,
)
