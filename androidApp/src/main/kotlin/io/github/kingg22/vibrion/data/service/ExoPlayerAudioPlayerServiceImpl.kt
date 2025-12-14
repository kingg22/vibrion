package io.github.kingg22.vibrion.data.service

import android.content.Context
import androidx.media3.session.MediaController
import io.github.kingg22.vibrion.MainApplication

class ExoPlayerAudioPlayerServiceImpl(androidContext: Context) : ExoPlayerAudioPlayerService(androidContext) {
    override fun controller(): MediaController? {
        val application = androidContext.applicationContext as? MainApplication
        return application?.getControllerOrNull()
    }
}
