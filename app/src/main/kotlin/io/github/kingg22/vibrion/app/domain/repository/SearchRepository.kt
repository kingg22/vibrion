package io.github.kingg22.vibrion.app.domain.repository

import io.github.kingg22.vibrion.app.domain.model.Single

interface SearchRepository {
    suspend fun searchSingles(query: String, limit: Int = 10): List<Single>
}
