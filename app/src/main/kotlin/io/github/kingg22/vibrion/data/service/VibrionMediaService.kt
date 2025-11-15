package io.github.kingg22.vibrion.data.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import co.touchlab.kermit.Logger
import io.github.kingg22.vibrion.MainActivity
import io.github.kingg22.vibrion.domain.service.AudioPlayerService
import org.koin.mp.KoinPlatform

@androidx.annotation.OptIn(UnstableApi::class)
class VibrionMediaService : MediaLibraryService() {
    private lateinit var mediaSession: MediaLibrarySession
    private lateinit var player: Player

    override fun onCreate() {
        super.onCreate()
        logger.i { "Creating Vibrion Media Service to persist player session" }

        player = (KoinPlatform.getKoin().get<AudioPlayerService>() as ExoPlayerAudioPlayerService)
            .getExoPlayer()

        mediaSession = MediaLibrarySession.Builder(this, player, VibrionSessionCallback())
            .setId("vibrion-root")
            .build()

        mediaSession.setSessionActivity(
            PendingIntent.getActivity(
                this,
                0,
                Intent(this, MainActivity::class.java),
                PendingIntent.FLAG_IMMUTABLE,
            ),
        )

        setListener(
            object : Listener {
                override fun onForegroundServiceStartNotAllowedException() {
                    logger.e { "Forbidden media services, verify permissions" }
                }
            },
        )
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? =
        if (::mediaSession.isInitialized) mediaSession else null

    override fun onDestroy() {
        mediaSession.release()
        player.release()
        super.onDestroy()
    }

    // TODO implement
    class VibrionSessionCallback : MediaLibrarySession.Callback

    companion object {
        private val logger = Logger.withTag("VibrionMediaService")

        @JvmStatic
        fun startService(context: Context) {
            logger.d { "Starting VibrionMediaService with intent and android context" }
            val intent = Intent(context, VibrionMediaService::class.java)
            ContextCompat.startForegroundService(context, intent)
        }
    }
}
