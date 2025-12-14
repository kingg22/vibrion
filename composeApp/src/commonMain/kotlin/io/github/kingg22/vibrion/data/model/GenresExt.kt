package io.github.kingg22.vibrion.data.model

import io.github.kingg22.deezer.client.api.objects.Genre
import io.github.kingg22.vibrion.domain.model.GenreInfo

fun Genre.toDomain() = GenreInfo(this.id, this.name, this.picture)
