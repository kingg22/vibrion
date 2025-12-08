package io.github.kingg22.vibrion.domain.usecase.search

import io.github.kingg22.vibrion.domain.repository.SearchHistoryRepository

@JvmInline
value class GetSearchHistoryUseCase(private val repository: SearchHistoryRepository) {
    operator fun invoke() = repository.getHistory()
}
