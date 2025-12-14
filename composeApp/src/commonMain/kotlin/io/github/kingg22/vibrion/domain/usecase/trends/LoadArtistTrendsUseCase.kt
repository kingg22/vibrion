package io.github.kingg22.vibrion.domain.usecase.trends

import io.github.kingg22.vibrion.domain.repository.TrendsRepository
import kotlin.jvm.JvmInline

@JvmInline
value class LoadArtistTrendsUseCase(private val repository: TrendsRepository) {
    operator fun invoke() = repository.buildArtistsPagedSource()
}
