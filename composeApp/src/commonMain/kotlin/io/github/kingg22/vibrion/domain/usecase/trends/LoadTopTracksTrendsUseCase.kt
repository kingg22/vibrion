package io.github.kingg22.vibrion.domain.usecase.trends

import io.github.kingg22.vibrion.domain.repository.TrendsRepository
import kotlin.jvm.JvmInline

@JvmInline
value class LoadTopTracksTrendsUseCase(private val trendsRepository: TrendsRepository) {
    operator fun invoke() = trendsRepository.buildTracksPagedSource()
}
