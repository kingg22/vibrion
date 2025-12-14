package io.github.kingg22.vibrion.domain.usecase.search

import io.github.kingg22.vibrion.domain.repository.SearchHistoryRepository
import kotlin.jvm.JvmInline

@JvmInline
value class SaveSearchQueryUseCase(private val repository: SearchHistoryRepository) {
    suspend operator fun invoke(query: String) {
        if (query.isNotBlank()) {
            repository.saveSearch(query)
        }
    }
}
