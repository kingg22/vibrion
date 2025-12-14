package io.github.kingg22.vibrion.data.service

import android.app.PendingIntent
import android.content.Intent
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import io.github.kingg22.vibrion.MainActivity

class VibrionMediaServiceImpl : VibrionMediaService() {

    @androidx.annotation.OptIn(UnstableApi::class)
    override fun onMediaSessionSetSession(mediaSession: MediaSession) {
        mediaSession.setSessionActivity(
            PendingIntent.getActivity(
                this,
                0,
                Intent(this, MainActivity::class.java),
                PendingIntent.FLAG_IMMUTABLE,
            ),
        )
    }
}
