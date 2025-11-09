package io.github.kingg22.vibrion.data.model

import io.github.kingg22.deezer.client.api.objects.Album
import io.github.kingg22.deezer.client.api.objects.ImageSizes
import io.github.kingg22.deezer.client.api.objects.retrieveImageUrl
import io.github.kingg22.deezer.client.api.objects.withImageSize
import io.github.kingg22.vibrion.domain.model.ArtistInfo
import io.github.kingg22.vibrion.domain.model.DownloadableAlbum
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter
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
    }?.takeIf { l -> l.isNotEmpty() }?.let {
        artist?.let { artist ->
            it + ArtistInfo(
                id = artist.id.toString(),
                name = artist.name,
                pictureUrl = artist.pictureBig,
            )
        } ?: it
    } ?: run {
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

fun LocalDateTime.toReadableString(): String {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")
    return this.toJavaLocalDateTime().format(formatter)
}

fun LocalDate.toReadableString(): String {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    return this.toJavaLocalDate().format(formatter)
}
