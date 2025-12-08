package io.github.kingg22.vibrion.data.model

import io.github.kingg22.deezer.client.api.objects.Artist
import io.github.kingg22.vibrion.domain.model.ArtistInfo

fun Artist.toDomain() = ArtistInfo(
    id = this.id.toString(),
    name = this.name,
    pictureUrl = this.picture,
)
