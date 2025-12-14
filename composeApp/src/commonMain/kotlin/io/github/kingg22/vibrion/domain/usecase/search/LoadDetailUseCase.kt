package io.github.kingg22.vibrion.domain.usecase.search

import io.github.kingg22.vibrion.domain.model.DownloadableItem
import io.github.kingg22.vibrion.domain.model.ModelType
import io.github.kingg22.vibrion.domain.model.ModelType.ALBUM
import io.github.kingg22.vibrion.domain.model.ModelType.ARTIST
import io.github.kingg22.vibrion.domain.model.ModelType.GENRE
import io.github.kingg22.vibrion.domain.model.ModelType.PLAYLIST
import io.github.kingg22.vibrion.domain.model.ModelType.SINGLE
import io.github.kingg22.vibrion.domain.model.ModelType.USER
import io.github.kingg22.vibrion.domain.repository.SearchRepository
import kotlin.jvm.JvmInline

@JvmInline
value class LoadDetailUseCase(private val repository: SearchRepository) {
    suspend operator fun invoke(type: ModelType, id: String): DownloadableItem? = when (type) {
        ALBUM -> repository.findAlbum(id)
        PLAYLIST -> repository.findPlaylist(id)
        SINGLE -> repository.findSingle(id)
        ARTIST, USER, GENRE -> error("Use other method, implementation error")
    }
}
