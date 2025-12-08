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
import coil3.annotation.ExperimentalCoilApi
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
import coil3.network.cachecontrol.CacheControlCacheStrategy
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import com.google.common.util.concurrent.ListenableFuture
import io.github.kingg22.vibrion.data.service.VibrionMediaService
import io.github.kingg22.vibrion.di.vibrionAppModule
import org.koin.android.ext.koin.androidContext
import org.koin.androix.startup.KoinStartup
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.logger.MESSAGE
import org.koin.dsl.KoinConfiguration
import kotlin.time.ExperimentalTime
import co.touchlab.kermit.Logger as KermitLogger
import coil3.util.Logger as Coil3Logger
import org.koin.android.ext.android.get as koinGet
import org.koin.core.logger.Level as KoinLoggerLevel
import org.koin.core.logger.Logger as KoinLogger

@OptIn(KoinExperimentalAPI::class)
class MainApplication :
    Application(),
    KoinStartup,
    SingletonImageLoader.Factory {
    private var controllerFuture: ListenableFuture<MediaController>? = null
    private var controller: MediaController? = null

    init {
        KermitLogger.mutableConfig.minSeverity = Severity.Verbose
    }

    @OptIn(ExperimentalTime::class, ExperimentalCoilApi::class)
    override fun newImageLoader(context: Context) = ImageLoader.Builder(context)
        .crossfade(true)
        .components {
            add(
                KtorNetworkFetcherFactory(
                    httpClient = { koinGet() },
                    cacheStrategy = { CacheControlCacheStrategy() },
                ),
            )
        }
        .logger(
            object : Coil3Logger {
                private val logger = KermitLogger.withTag("Coil3")

                override var minLevel: Coil3Logger.Level
                    get() = when (logger.mutableConfig.minSeverity) {
                        Severity.Verbose -> Coil3Logger.Level.Verbose
                        Severity.Debug -> Coil3Logger.Level.Debug
                        Severity.Info -> Coil3Logger.Level.Info
                        Severity.Warn -> Coil3Logger.Level.Warn
                        Severity.Error -> Coil3Logger.Level.Error
                        Severity.Assert -> Coil3Logger.Level.Error
                    }
                    set(value) {
                        // NO Op
                    }

                override fun log(tag: String, level: Coil3Logger.Level, message: String?, throwable: Throwable?) {
                    when (level) {
                        Coil3Logger.Level.Verbose -> logger.v(message ?: "", throwable, tag)

                        Coil3Logger.Level.Debug -> logger.d(message ?: "", throwable, tag)

                        // Logs as verbose because coil3 have a lot of logs in level Info
                        Coil3Logger.Level.Info -> logger.v(message ?: "", throwable, tag)

                        Coil3Logger.Level.Warn -> logger.w(message ?: "", throwable, tag)

                        Coil3Logger.Level.Error -> logger.e(message ?: "", throwable, tag)
                    }
                }
            },
        )
        .memoryCache {
            MemoryCache.Builder()
                .maxSizePercent(context, 0.25)
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(context.cacheDir.resolve("image_cache"))
                .maxSizePercent(0.02)
                .build()
        }
        .build()

    override fun onKoinStartup() = KoinConfiguration {
        // Log Koin into Android logger
        logger(
            object : KoinLogger() {
                private val logger = KermitLogger.withTag("koin")

                init {
                    this.level = when (logger.mutableConfig.minSeverity) {
                        Severity.Verbose -> KoinLoggerLevel.DEBUG
                        Severity.Debug -> KoinLoggerLevel.DEBUG
                        Severity.Info -> KoinLoggerLevel.INFO
                        Severity.Warn -> KoinLoggerLevel.WARNING
                        Severity.Error -> KoinLoggerLevel.ERROR
                        Severity.Assert -> KoinLoggerLevel.ERROR
                    }
                }

                override fun display(level: KoinLoggerLevel, msg: MESSAGE) {
                    when (level) {
                        KoinLoggerLevel.DEBUG -> logger.d(msg)

                        KoinLoggerLevel.INFO -> logger.i(msg)

                        KoinLoggerLevel.WARNING -> logger.w(msg)

                        KoinLoggerLevel.ERROR -> logger.e(msg)

                        KoinLoggerLevel.NONE -> {
                            // do nothing
                        }
                    }
                }
            },
        )
        // Reference Android context
        androidContext(this@MainApplication)
        // Load modules
        modules(vibrionAppModule) // specific module for android
    }

    /**
     * Devuelve el MediaController, inicializándolo si es necesario.
     * Usas un StateFlow si quieres observarlo desde la UI.
     */
    fun getMediaController(context: Context): ListenableFuture<MediaController> {
        val appContext = context.applicationContext
        val existing = controllerFuture
        if (existing != null) return existing

        val sessionToken = SessionToken(appContext, ComponentName(appContext, VibrionMediaService::class.java))

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
