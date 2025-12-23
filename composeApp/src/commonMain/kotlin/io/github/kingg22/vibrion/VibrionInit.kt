package io.github.kingg22.vibrion

import co.touchlab.kermit.Severity
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.annotation.ExperimentalCoilApi
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.network.cachecontrol.CacheControlCacheStrategy
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import okio.Path
import org.koin.core.logger.MESSAGE
import org.koin.mp.KoinPlatform
import kotlin.time.ExperimentalTime
import co.touchlab.kermit.Logger as KermitLogger
import coil3.util.Logger as Coil3Logger
import org.koin.core.logger.Level as KoinLoggerLevel
import org.koin.core.logger.Logger as KoinLogger

/** Entrypoint for coil3 image loader */
@OptIn(ExperimentalTime::class, ExperimentalCoilApi::class)
fun buildCoilImageLoader(context: PlatformContext) = ImageLoader.Builder(context)
    .crossfade(true)
    .components {
        add(
            KtorNetworkFetcherFactory(
                httpClient = { KoinPlatform.getKoin().get() },
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
            .directory(coilCacheDir(context))
            .maxSizePercent(0.02)
            .build()
    }
    .build()

const val COIL_CACHE_DIR = "image_cache"

expect fun coilCacheDir(context: PlatformContext): Path

class KoinKermitLogger : KoinLogger() {
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
}
