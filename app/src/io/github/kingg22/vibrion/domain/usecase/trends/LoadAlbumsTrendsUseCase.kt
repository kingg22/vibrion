package io.github.kingg22.vibrion.domain.usecase.trends

import io.github.kingg22.vibrion.domain.repository.TrendsRepository

@JvmInline
value class LoadAlbumsTrendsUseCase(private val repository: TrendsRepository) {
    operator fun invoke() = repository.buildAlbumsPagedSource()
}
