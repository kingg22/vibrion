package io.github.kingg22.vibrion

import android.app.Application
import android.content.Context
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
import io.github.kingg22.deezer.client.utils.ExperimentalDeezerClient
import io.github.kingg22.deezer.client.utils.HttpClientBuilder
import io.github.kingg22.vibrion.di.vibrionAppModule
import io.ktor.client.plugins.HttpRedirect
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.FileStorage
import io.ktor.http.HttpStatusCode
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

    init {
        KermitLogger.mutableConfig.minSeverity = if (BuildConfig.DEBUG) Severity.Verbose else Severity.Info
    }

    @OptIn(ExperimentalTime::class, ExperimentalCoilApi::class, ExperimentalDeezerClient::class)
    override fun newImageLoader(context: Context) = ImageLoader.Builder(context)
        .crossfade(true)
        .components {
            add(
                KtorNetworkFetcherFactory(
                    httpClient = {
                        koinGet<HttpClientBuilder>()
                            .addCustomConfig {
                                // Change default configuration to handle cache, needs to document or change in deezer api
                                install(HttpRedirect)
                                install(HttpCache) {
                                    publicStorage(FileStorage(context.cacheDir.resolve("ktor_image_cache")))
                                }
                                expectSuccess = false
                                HttpResponseValidator {
                                    handleResponseExceptionWithRequest { cause, _ ->
                                        if (cause is RedirectResponseException &&
                                            cause.response.status == HttpStatusCode.NotModified
                                        ) {
                                            return@handleResponseExceptionWithRequest
                                        }
                                        throw cause
                                    }
                                }
                            }.build()
                    },
                    cacheStrategy = { CacheControlCacheStrategy() },
                ),
            )
        }
        .logger(
            if (BuildConfig.DEBUG) {
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
                            when (value) {
                                Coil3Logger.Level.Verbose -> logger.mutableConfig.minSeverity = Severity.Verbose
                                Coil3Logger.Level.Debug -> logger.mutableConfig.minSeverity = Severity.Debug
                                Coil3Logger.Level.Info -> logger.mutableConfig.minSeverity = Severity.Info
                                Coil3Logger.Level.Warn -> logger.mutableConfig.minSeverity = Severity.Warn
                                Coil3Logger.Level.Error -> logger.mutableConfig.minSeverity = Severity.Error
                            }
                        }

                    override fun log(tag: String, level: Coil3Logger.Level, message: String?, throwable: Throwable?) {
                        when (level) {
                            Coil3Logger.Level.Verbose -> logger.v(message ?: "", throwable, tag)
                            Coil3Logger.Level.Debug -> logger.d(message ?: "", throwable, tag)
                            Coil3Logger.Level.Info -> logger.i(message ?: "", throwable, tag)
                            Coil3Logger.Level.Warn -> logger.w(message ?: "", throwable, tag)
                            Coil3Logger.Level.Error -> logger.e(message ?: "", throwable, tag)
                        }
                    }
                }
            } else {
                null
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
            if (BuildConfig.DEBUG) {
                object : KoinLogger() {
                    private val logger = KermitLogger.withTag("koin")

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
            } else {
                object : KoinLogger() {
                    override fun display(level: KoinLoggerLevel, msg: MESSAGE) {
                        // No Op
                    }
                }
            },
        )
        // Reference Android context
        androidContext(this@MainApplication)
        // Load modules
        modules(vibrionAppModule) // specific module for android
    }
}
