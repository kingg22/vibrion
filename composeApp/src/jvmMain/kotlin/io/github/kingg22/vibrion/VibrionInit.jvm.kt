package io.github.kingg22.vibrion

import coil3.PlatformContext
import okio.Path
import okio.Path.Companion.toOkioPath
import kotlin.io.path.Path

actual fun coilCacheDir(context: PlatformContext): Path =
    if (System.getProperty("os.name").contains("Windows", ignoreCase = true)) {
        Path(System.getenv("APPDATA"), "Vibrion", COIL_CACHE_DIR).toOkioPath()
    } else {
        Path(System.getProperty("user.home"), ".config", "Vibrion", COIL_CACHE_DIR).toOkioPath()
    }
