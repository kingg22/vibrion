package io.github.kingg22.vibrion.data.service

import io.github.kingg22.vibrion.domain.model.DownloadableItem
import io.github.kingg22.vibrion.domain.model.PlaybackState
import io.github.kingg22.vibrion.domain.service.AudioPlayerService
import kotlinx.coroutines.flow.StateFlow

class AudioPlayerServiceNoOpImpl : AudioPlayerService {
    override val playbackState: StateFlow<PlaybackState>
        get() = TODO()

    override fun setQueue(queue: List<DownloadableItem>, startIndex: Int) {
    }

    override fun play() {
    }

    override fun pause() {
    }

    override fun hasNext(): Boolean = false

    override fun next() {
    }

    override fun previous() {
    }

    override fun setTrack(track: DownloadableItem) {
    }

    override fun enqueue(track: DownloadableItem) {
    }

    override fun removeFromQueue(trackId: String) {
    }
}
