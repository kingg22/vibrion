package io.github.kingg22.vibrion.domain.model

sealed class PlaybackState {
    data object Idle : PlaybackState()
    data object Ended : PlaybackState()
    data object Paused : PlaybackState()
    data object Playing : PlaybackState()
    data class Error(val message: String, val exception: Exception?) : PlaybackState()
    data object Buffering : PlaybackState()
}
