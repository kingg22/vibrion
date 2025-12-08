package io.github.kingg22.vibrion.data.service

import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import io.github.kingg22.vibrion.MainApplication
import io.github.kingg22.vibrion.domain.model.DownloadableItem
import io.github.kingg22.vibrion.domain.model.PlaybackState
import io.github.kingg22.vibrion.domain.service.AudioPlayerService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import android.content.Context as AndroidContext

class ExoPlayerAudioPlayerService(private val androidContext: AndroidContext) : AudioPlayerService {
    private val exo = ExoPlayer.Builder(androidContext).build()
    private val _playbackState = MutableStateFlow<PlaybackState>(PlaybackState.Idle)

    override val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    init {
        exo.addListener(object : Player.Listener {

            override fun onPlaybackStateChanged(state: Int) {
                when (state) {
                    Player.STATE_BUFFERING -> {
                        _playbackState.update { _ -> PlaybackState.Buffering }
                    }

                    Player.STATE_READY -> {
                        if (exo.isPlaying) {
                            _playbackState.update { _ -> PlaybackState.Playing }
                        } else {
                            _playbackState.update { _ -> PlaybackState.Paused }
                        }
                    }

                    Player.STATE_ENDED -> {
                        _playbackState.update { _ -> PlaybackState.Ended }
                    }

                    Player.STATE_IDLE -> {
                        _playbackState.update { _ -> PlaybackState.Idle }
                    }
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _playbackState.update { _ ->
                    if (isPlaying) PlaybackState.Playing else PlaybackState.Paused
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                _playbackState.update { _ ->
                    PlaybackState.Error(error.message ?: error.errorCodeName, error)
                }
            }
        })
    }

    /** This ONLY is required to other media3 APIs */
    internal fun getExoPlayer() = exo

    private fun controller(): MediaController? {
        val app = androidContext.applicationContext as MainApplication
        return app.getControllerOrNull()
    }

    override fun setQueue(queue: List<DownloadableItem>, startIndex: Int) {
        controller()?.stop()
        controller()?.clearMediaItems()

        val mediaItems = queue.map { it.toMediaItem() }
        controller()?.setMediaItems(mediaItems, startIndex, 0L)

        VibrionMediaService.startService(androidContext)
        controller()?.prepare()
        controller()?.playWhenReady = true
    }

    override fun play() {
        VibrionMediaService.startService(androidContext)
        controller()?.play()
    }

    override fun pause() {
        controller()?.pause()
    }

    override fun hasNext(): Boolean = controller()?.hasNextMediaItem() == true

    override fun next() {
        controller()?.seekToNext()
    }

    override fun previous() {
        controller()?.seekToPrevious()
    }

    override fun setTrack(track: DownloadableItem) {
        controller()?.setMediaItem(track.toMediaItem())
        controller()?.prepare()
    }

    override fun enqueue(track: DownloadableItem) {
        val item = track.toMediaItem()
        controller()?.addMediaItem(item)
    }

    override fun removeFromQueue(trackId: String) {
        val index = controller()?.currentTimeline
            ?.let { timeline ->
                (0 until timeline.windowCount).firstOrNull { i ->
                    controller()?.getMediaItemAt(i)?.mediaId == trackId
                }
            }

        if (index != null) {
            controller()?.removeMediaItem(index)
        }
    }
}

private fun DownloadableItem.toMediaItem(): MediaItem {
    require(this is DownloadableItem.StreamableItem) {
        "DownloadableItem must be StreamableItem to be played"
    }

    val metadata = MediaMetadata.Builder()
        .setTitle(title)
        .setDescription(description)
        .setArtworkUri(thumbnailUrl?.toUri())
        .setAlbumTitle(album)
        .setArtist(artists.joinToString { it.name })
        .build()

    return MediaItem.Builder()
        .setMediaId(id)
        .setUri(streamUrl)
        .setMediaMetadata(metadata)
        .build()
}
