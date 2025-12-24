package io.github.kingg22.vibrion

import android.app.Application
import android.content.ComponentName
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import co.touchlab.kermit.Severity
import coil3.ImageLoader
import coil3.SingletonImageLoader
import com.google.common.util.concurrent.ListenableFuture
import io.github.kingg22.vibrion.data.service.ExoPlayerAudioPlayerServiceImpl
import io.github.kingg22.vibrion.data.service.VibrionMediaServiceImpl
import io.github.kingg22.vibrion.di.vibrionAppModule
import io.github.kingg22.vibrion.domain.service.AudioPlayerService
import org.koin.android.ext.koin.androidContext
import org.koin.androix.startup.KoinStartup
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.module
import co.touchlab.kermit.Logger as KermitLogger

@OptIn(KoinExperimentalAPI::class)
class MainApplication :
    Application(),
    KoinStartup,
    SingletonImageLoader.Factory {
    private var controllerFuture: ListenableFuture<MediaController>? = null
    private var controller: MediaController? = null

    init {
        KermitLogger.mutableConfig.minSeverity = if (BuildConfig.DEBUG) Severity.Verbose else Severity.Info
    }

    override fun newImageLoader(context: Context): ImageLoader = buildCoilImageLoader(context)

    override fun onKoinStartup(): KoinConfiguration = KoinConfiguration {
        // Log Koin into Kermit logger
        logger(KoinKermitLogger())
        // Reference Android context
        androidContext(this@MainApplication)
        modules(
            // Load modules of common
            vibrionAppModule,
            // specific module for android
            module {
                single<AudioPlayerService> { _ -> ExoPlayerAudioPlayerServiceImpl(androidContext()) }
            },
        )
    }

    /**
     * Devuelve el MediaController, inicializándolo si es necesario.
     * Usas un StateFlow si quieres observarlo desde la UI.
     */
    fun getMediaController(context: Context): ListenableFuture<MediaController> {
        val appContext = context.applicationContext
        val existing = controllerFuture
        if (existing != null) return existing

        val sessionToken = SessionToken(appContext, ComponentName(appContext, VibrionMediaServiceImpl::class.java))

        val future = MediaController.Builder(appContext, sessionToken)
            .buildAsync()

        controllerFuture = future

        future.addListener(
            {
                controller = future.get()
            },
            ContextCompat.getMainExecutor(appContext),
        )

        return future
    }

    fun getControllerOrNull() = controller

    override fun onTerminate() {
        controllerFuture?.let { MediaController.releaseFuture(it) }
        controllerFuture = null
        controller = null
        super.onTerminate()
    }
}
