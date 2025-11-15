package io.github.kingg22.vibrion.data.model

import io.github.kingg22.deezer.client.api.objects.ImageSizes
import io.github.kingg22.deezer.client.api.objects.Track
import io.github.kingg22.deezer.client.api.objects.retrieveImageUrl
import io.github.kingg22.deezer.client.api.objects.withImageSize
import io.github.kingg22.vibrion.domain.model.DownloadableSingle
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun Track.toDomain() = DownloadableSingle(
    id = this.id.toString(),
    title = this.title,
    description = null,
    thumbnailUrl = md5Image?.let { this.retrieveImageUrl().withImageSize(ImageSizes.BIG) },
    streamUrl = this.preview,
    releaseDate = this.releaseDate?.toReadableString(),
    duration = this.duration.toDuration(DurationUnit.SECONDS),
    artists = this.contributors?.map { it.toDomain() } ?: listOf(this.artist.toDomain()),
    album = this.album?.title,
)
