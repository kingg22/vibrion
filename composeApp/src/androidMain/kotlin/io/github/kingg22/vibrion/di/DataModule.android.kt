package io.github.kingg22.vibrion.di

import android.content.Context
import io.ktor.client.plugins.cache.storage.CacheStorage
import io.ktor.client.plugins.cache.storage.FileStorage
import org.koin.core.scope.Scope

actual fun Scope.cacheDirFor(dir: String): CacheStorage = FileStorage(get<Context>().cacheDir.resolve(dir))
