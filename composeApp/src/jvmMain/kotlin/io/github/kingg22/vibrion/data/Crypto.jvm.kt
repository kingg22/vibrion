package io.github.kingg22.vibrion.data

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import kotlin.io.path.Path

@Suppress("kotlin:S5542") // TODO review this warning about encrypt padding
actual object Crypto {
    private const val KEY_ALIAS = "secret"
    private const val TRANSFORMATION = "AES/CBC/PKCS5Padding" // JVM usa PKCS5, equivalente a PKCS7
    private const val KEYSTORE_TYPE = "JCEKS" // o "PKCS12"
    private val KEYSTORE_FILE: File by lazy {
        if (System.getProperty("os.name").contains("Windows", ignoreCase = true)) {
            Path(System.getenv("APPDATA"), "Vibrion", "keystore.jceks").toFile()
        } else {
            Path(System.getProperty("user.home"), ".config", "Vibrion", "keystore.jceks").toFile()
        }
    }
    private const val STORE_PASSWORD = "storepass"
    private const val KEY_PASSWORD = "keypass"

    private val keyStore: KeyStore by lazy {
        KeyStore.getInstance(KEYSTORE_TYPE).apply {
            if (KEYSTORE_FILE.exists()) {
                load(FileInputStream(KEYSTORE_FILE), STORE_PASSWORD.toCharArray())
            } else {
                load(null, null)
                store(FileOutputStream(KEYSTORE_FILE), STORE_PASSWORD.toCharArray())
            }
        }
    }

    private fun getKey(): SecretKey? {
        val entry: KeyStore.Entry? = keyStore.getEntry(
            KEY_ALIAS,
            KeyStore.PasswordProtection(KEY_PASSWORD.toCharArray()),
        )
        return (entry as? KeyStore.SecretKeyEntry)?.secretKey
    }

    private fun createKey(): SecretKey {
        val keyGen: KeyGenerator = KeyGenerator.getInstance("AES")
        keyGen.init(256) // o 128 según necesidad
        val secretKey: SecretKey = keyGen.generateKey()
        keyStore.setEntry(
            KEY_ALIAS,
            KeyStore.SecretKeyEntry(secretKey),
            KeyStore.PasswordProtection(KEY_PASSWORD.toCharArray()),
        )
        keyStore.store(FileOutputStream(KEYSTORE_FILE), STORE_PASSWORD.toCharArray())
        return secretKey
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun requireKey() = getKey() ?: createKey()

    actual fun encrypt(data: String): ByteArray {
        val cipher: Cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, requireKey())
        val iv: ByteArray = cipher.iv
        val encrypted: ByteArray = cipher.doFinal(data.toByteArray())
        return iv + encrypted
    }

    actual fun decrypt(data: ByteArray): String {
        val cipher: Cipher = Cipher.getInstance(TRANSFORMATION)
        val iv = data.copyOfRange(0, cipher.blockSize)
        val encrypted = data.copyOfRange(cipher.blockSize, data.size)
        cipher.init(Cipher.DECRYPT_MODE, requireKey(), IvParameterSpec(iv))
        return String(cipher.doFinal(encrypted))
    }
}
