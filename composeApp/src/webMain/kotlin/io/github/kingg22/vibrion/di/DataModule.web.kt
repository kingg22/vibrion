package io.github.kingg22.vibrion.di

import io.ktor.client.plugins.cache.storage.CacheStorage
import org.koin.core.scope.Scope

actual fun Scope.cacheDirFor(dir: String): CacheStorage = CacheStorage.Unlimited()
