package io.github.kingg22.vibrion.di

import io.ktor.client.plugins.cache.storage.CacheStorage
import io.ktor.client.plugins.cache.storage.FileStorage
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.Scope

actual fun Scope.cacheDirFor(dir: String): CacheStorage = FileStorage(androidContext().cacheDir.resolve(dir))
