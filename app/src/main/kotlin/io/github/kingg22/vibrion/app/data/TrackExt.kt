package io.github.kingg22.vibrion.app.data

import io.github.kingg22.deezerSdk.api.objects.ImageSizes
import io.github.kingg22.deezerSdk.api.objects.Track
import io.github.kingg22.deezerSdk.api.objects.retrieveImageUrl
import io.github.kingg22.deezerSdk.api.objects.withImageSize
import io.github.kingg22.vibrion.app.domain.model.Single
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun Track.toDomain() = Single(
    id = this.id.toString(),
    title = this.title,
    description = this.titleShort,
    thumbnailUrl = runCatching { this.retrieveImageUrl().withImageSize(ImageSizes.MEDIUM) }.getOrNull(),
    mediaUrl = this.preview,
    releaseDate = this.releaseDate?.toString(),
    duration = this.duration.toDuration(DurationUnit.SECONDS),
    artists = listOf(this.artist.toDomain()),
    explicit = this.explicitLyrics,
)
