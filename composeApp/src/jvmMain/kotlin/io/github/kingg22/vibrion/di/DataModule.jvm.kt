package io.github.kingg22.vibrion.di

import io.ktor.client.plugins.cache.storage.CacheStorage
import io.ktor.client.plugins.cache.storage.FileStorage
import org.koin.core.scope.Scope
import kotlin.io.path.Path

actual fun Scope.cacheDirFor(dir: String): CacheStorage = FileStorage(
    directory = if (System.getProperty("os.name").contains("Windows", ignoreCase = true)) {
        Path(System.getenv("APPDATA"), "Vibrion", dir).toFile()
    } else {
        Path(System.getProperty("user.home"), ".config", "Vibrion", dir).toFile()
    },
)
