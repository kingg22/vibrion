package io.github.kingg22.vibrion.data

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

object Crypto {
    private const val KEY_ALIAS = "secret"
    private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
    private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
    private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
    private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private val keyStore: KeyStore by lazy { KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) } }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun getKey() = (keyStore.getEntry(KEY_ALIAS, null) as? KeyStore.SecretKeyEntry)?.secretKey

    @Suppress("NOTHING_TO_INLINE")
    private inline fun requireKey() = getKey() ?: createKey()

    private fun createKey(): SecretKey = KeyGenerator.getInstance(ALGORITHM)
        .apply {
            init(
                KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
                )
                    .setBlockModes(BLOCK_MODE)
                    .setEncryptionPaddings(PADDING)
                    .setRandomizedEncryptionRequired(true)
                    .setUserAuthenticationRequired(false)
                    .build(),
            )
        }
        .generateKey()

    fun encrypt(data: String): ByteArray {
        val cipher: Cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, requireKey())
        val iv: ByteArray = cipher.iv
        val encrypted: ByteArray = cipher.doFinal(data.toByteArray())
        return iv + encrypted
    }

    fun decrypt(data: ByteArray): String {
        val cipher: Cipher = Cipher.getInstance(TRANSFORMATION)
        val iv = data.copyOfRange(0, cipher.blockSize)
        val dataDecrypted = data.copyOfRange(cipher.blockSize, data.size)
        cipher.init(Cipher.DECRYPT_MODE, requireKey(), IvParameterSpec(iv))
        return String(cipher.doFinal(dataDecrypted))
    }
}
