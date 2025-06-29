package io.github.kingg22.vibrion.app.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class CryptoTest {

    @Test
    fun testEncryptDecrypt() {
        val token = "1234567890abcdef"
        val encrypted = Crypto.encrypt(token)
        val decrypted = Crypto.decrypt(encrypted)
        assertEquals(token, decrypted)
    }
}
