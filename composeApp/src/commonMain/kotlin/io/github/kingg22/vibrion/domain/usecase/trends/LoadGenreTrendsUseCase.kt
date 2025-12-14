package io.github.kingg22.vibrion.domain.usecase.trends

import io.github.kingg22.vibrion.domain.repository.TrendsRepository
import kotlin.jvm.JvmInline

@JvmInline
value class LoadGenreTrendsUseCase(private val trendsRepository: TrendsRepository) {
    operator fun invoke() = trendsRepository.buildGenresPagedSource()
}
