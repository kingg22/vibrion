package io.github.kingg22.vibrion.domain.service

import io.github.kingg22.vibrion.domain.model.DownloadableItem
import io.github.kingg22.vibrion.domain.model.PlaybackState
import kotlinx.coroutines.flow.StateFlow

interface AudioPlayerService {
    val playbackState: StateFlow<PlaybackState>

    /** Reemplaza toda la cola y empieza desde [startIndex] */
    suspend fun setQueue(queue: List<DownloadableItem>, startIndex: Int = 0)
    fun play()
    fun pause()
    operator fun hasNext(): Boolean
    operator fun next()
    fun previous()

    /** Agregar al inicio de la cola */
    fun setTrack(track: DownloadableItem)

    /** Agregar al final de la cola */
    fun enqueue(track: DownloadableItem)

    /** Remover por trackId (mediaId) */
    fun removeFromQueue(trackId: String)
}
