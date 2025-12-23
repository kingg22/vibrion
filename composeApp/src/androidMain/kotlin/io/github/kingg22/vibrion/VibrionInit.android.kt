package io.github.kingg22.vibrion

import coil3.PlatformContext
import okio.Path
import okio.Path.Companion.toOkioPath

actual fun coilCacheDir(context: PlatformContext): Path = context.cacheDir.resolve(COIL_CACHE_DIR).toOkioPath()
