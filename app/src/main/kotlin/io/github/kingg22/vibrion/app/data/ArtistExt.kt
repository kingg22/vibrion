package io.github.kingg22.vibrion.app.data

import io.github.kingg22.deezer.client.api.objects.Artist
import io.github.kingg22.vibrion.app.domain.model.Artist as ArtistDomain

fun Artist.toDomain() = ArtistDomain(
    id = this.id.toString(),
    name = this.name,
    pictureUrl = this.picture,
)
