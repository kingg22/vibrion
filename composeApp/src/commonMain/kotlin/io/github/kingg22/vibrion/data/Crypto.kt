package io.github.kingg22.vibrion.data

expect object Crypto {
    fun encrypt(@Suppress("unused") data: String): ByteArray
    fun decrypt(@Suppress("unused") data: ByteArray): String
}
