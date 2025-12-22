package io.github.kingg22.vibrion.data

import io.ktor.utils.io.core.toByteArray

// STUB FILE
actual object Crypto {
    actual fun encrypt(data: String): ByteArray = data.toByteArray()

    actual fun decrypt(data: ByteArray): String = data.decodeToString()
}
