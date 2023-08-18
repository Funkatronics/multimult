package com.funkatronics.encoders

import com.funkatronics.encoders.error.InvalidInputException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Base58Tests {

    @Test
    fun testBase58EncodeDecode() {
        // given
        val testString: String = "Hello World"
        val expectedEncoded = "JxF12TrwUP45BMd"

        // when
        val actualEncoded: String = Base58.encodeToString(testString.toByteArray())
        val actualDecoded = Base58.decodeToString(actualEncoded)

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertEquals(testString, actualDecoded)
    }

    @Test
    fun testBase58EncodeDecodeLeadingZeros() {
        // given
        val leadingZeros = 3
        val testString: String = String(ByteArray(leadingZeros)) + "Hello World"
        val expectedEncoded = "111JxF12TrwUP45BMd"

        // when
        val actualEncoded: String = Base58.encodeToString(testString.toByteArray())
        val actualDecoded = Base58.decodeToString(actualEncoded)

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertEquals(testString, actualDecoded)
    }

    @Test
    fun testBase58EncodeDecodeEmptyString() {
        // given
        val testString = ""
        val expectedEncoded = ""

        // when
        val actualEncoded: String = Base58.encodeToString(testString.toByteArray())
        val actualDecoded = Base58.decodeToString(actualEncoded)

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertEquals(testString, actualDecoded)
    }

    @Test
    fun testBase58DecodeInvalidBase58() {
        // given
        val testString = "This is not valid Base58"

        // when
        val result = runCatching { Base58.decode(testString) }

        // then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is InvalidInputException.InvalidCharacter)
    }

    @Test
    fun testBase58RFCTestVector() {
        // given
        val testVector = mapOf(
            "Hello World!".toByteArray() to "2NEpo7TZRRrLZSi2U",
            "The quick brown fox jumps over the lazy dog.".toByteArray() to
                    "USm3fpXnKG5EUBx2ndxBDMPVciP5hGey2Jh4NDv6gmeo1LkMeiKrLJUUBk6Z",
            byteArrayOf(0x00, 0x00, 0x28, 0x7f, 0xb4.toByte(), 0xcd.toByte()) to "11233QC4",
        )

        for (entry in testVector) {

            // when
            val actualEncoded: String = Base58.encodeToString(entry.key)
            val actualDecoded = Base58.decodeToString(actualEncoded)

            // then
            assertEquals(entry.value, actualEncoded)
            assertEquals(String(entry.key), actualDecoded)
        }
    }
}