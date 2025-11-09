package io.github.kingg22.vibrion.domain.usecase.search

import io.github.kingg22.vibrion.domain.repository.SearchHistoryRepository

@JvmInline
value class ClearSearchHistoryUseCase(private val repository: SearchHistoryRepository) {
    suspend operator fun invoke() = repository.clearHistory()
}
